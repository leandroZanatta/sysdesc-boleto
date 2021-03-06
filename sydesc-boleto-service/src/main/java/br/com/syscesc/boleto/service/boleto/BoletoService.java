package br.com.syscesc.boleto.service.boleto;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.Aceite;

import br.com.boletos.service.BancoBoletoService;
import br.com.boletos.service.factory.BancoBoletoFactory;
import br.com.sysdesc.boleto.repository.dao.BoletoDAO;
import br.com.sysdesc.boleto.repository.model.Banco;
import br.com.sysdesc.boleto.repository.model.BoletoDadosCliente;
import br.com.sysdesc.boleto.repository.model.BoletoDadosPagamento;
import br.com.sysdesc.boleto.repository.model.BoletoDadosSacadorAvalista;
import br.com.sysdesc.boleto.repository.model.ConfiguracaoBoleto;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.classes.LongUtil;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.enumeradores.TipoStatusBoletoEnum;
import br.com.sysdesc.util.enumeradores.TipoTituloEnum;
import br.com.sysdesc.util.exception.SysDescException;
import br.com.sysdesc.util.vo.PagamentoBoletoVO;

public class BoletoService {

    private static final long DIAS_CORRIDOS = 1L;

    private BancoBoletoFactory bancoBoletoFactory = new BancoBoletoFactory();

    private BoletoDAO boletoDAO = new BoletoDAO();

    public br.com.sysdesc.boleto.repository.model.Boleto gerarBoletoComAceite(BoletoDadosCliente cliente, BoletoDadosSacadorAvalista avalista,
            PagamentoBoletoVO pagamentoBoleto, Banco banco, TipoTituloEnum tipoTitulo) {

        return gerarBoleto(cliente, avalista, pagamentoBoleto, banco, tipoTitulo, Aceite.A);
    }

    public br.com.sysdesc.boleto.repository.model.Boleto gerarBoletoSemAceite(BoletoDadosCliente cliente, BoletoDadosSacadorAvalista avalista,
            PagamentoBoletoVO pagamentoBoleto, Banco banco, TipoTituloEnum tipoTitulo) {

        return gerarBoleto(cliente, avalista, pagamentoBoleto, banco, tipoTitulo, Aceite.N);
    }

    private br.com.sysdesc.boleto.repository.model.Boleto gerarBoleto(BoletoDadosCliente cliente, BoletoDadosSacadorAvalista avalista,
            PagamentoBoletoVO pagamentoBoleto, Banco banco, TipoTituloEnum tipoTitulo, Aceite aceite) {

        ConfiguracaoBoleto configuracao = bancoBoletoFactory.buscarConfiguracaoBoleto(banco.getIdBanco());
        BancoBoletoService bancoGeracao = bancoBoletoFactory.getBancoBoleto(banco);

        String nossoNumero = bancoGeracao.gerarNossoNumero();
        Date dataDocumento = new Date();

        Titulo titulo = this.gerarTitulo(bancoGeracao, pagamentoBoleto, tipoTitulo, aceite, this.gerarSacado(cliente), nossoNumero, dataDocumento);

        Boleto boleto = new Boleto(titulo);
        boleto.setLocalPagamento("Pagável  em qualquer Banco até o Vencimento.");
        boleto.setInstrucaoAoSacado("");

        this.gerarInstrucoesSacado(boleto, pagamentoBoleto.getInstrucoes());

        byte[] arquivoBoleto = new BoletoViewer(boleto).getPdfAsByteArray();

        bancoBoletoFactory.atualizarNossoNumero(configuracao);

        br.com.sysdesc.boleto.repository.model.Boleto modeloBoleto = new br.com.sysdesc.boleto.repository.model.Boleto();

        modeloBoleto.setArquivo(arquivoBoleto);
        modeloBoleto.setConfiguracaoBoleto(configuracao);
        modeloBoleto.setNumeroBanco(banco.getNumeroBanco());
        modeloBoleto.setDataCadastro(dataDocumento);
        modeloBoleto.setDataVencimento(pagamentoBoleto.getDataVencimento());
        modeloBoleto.setNossoNumero(nossoNumero);
        modeloBoleto.setCodigoBarras(boleto.getCodigoDeBarras().write());
        modeloBoleto.setValorBoleto(pagamentoBoleto.getValorPagamento());
        modeloBoleto.setCodigoStatus(TipoStatusBoletoEnum.GERADO.getCodigo());
        modeloBoleto.setBoletoDadosCliente(cliente);
        modeloBoleto.setAceite(aceite.name());
        modeloBoleto.setEspecieTitulo(tipoTitulo.getCodigo().longValue());
        modeloBoleto.setBoletoDadosSacadorAvalista(avalista);
        modeloBoleto.setBoletoDadosPagamento(this.montarDadosPagamento(configuracao, pagamentoBoleto.getDataVencimento()));

        if (modeloBoleto.getBoletoDadosSacadorAvalista() != null) {
            modeloBoleto.getBoletoDadosSacadorAvalista().setBoleto(modeloBoleto);
        }

        modeloBoleto.getBoletoDadosCliente().setBoleto(modeloBoleto);
        modeloBoleto.getBoletoDadosPagamento().setBoleto(modeloBoleto);

        boletoDAO.salvar(modeloBoleto);

        return modeloBoleto;
    }

    private BoletoDadosPagamento montarDadosPagamento(ConfiguracaoBoleto configuracao, Date dataVencimento) {

        Long codigoProtesto = configuracao.getCodigoProtesto();

        BoletoDadosPagamento boletoDadosPagamento = new BoletoDadosPagamento();
        boletoDadosPagamento.setCodigoCarteira(configuracao.getCodigoCarteira());
        boletoDadosPagamento.setModalidade(configuracao.getModalidade());
        boletoDadosPagamento.setCodigoProtesto(codigoProtesto);
        boletoDadosPagamento.setCodigoJurosMora(configuracao.getCodigoJurosMora());
        boletoDadosPagamento.setCodigoMulta(configuracao.getCodigoMulta());

        boletoDadosPagamento.setDiasProtesto(codigoProtesto.equals(DIAS_CORRIDOS) ? configuracao.getDiasProtesto() : 0L);
        boletoDadosPagamento.setDataLimitePagamento(DateUtil.addDays(dataVencimento, configuracao.getDiasMaximoPagamento()));

        boletoDadosPagamento.setDataJurosMora(calcularDataBaseDias(dataVencimento, configuracao.getDiasJurosMora()));
        boletoDadosPagamento.setDataMulta(calcularDataBaseDias(dataVencimento, configuracao.getDiasMulta()));

        boletoDadosPagamento.setValorJurosMora(configuracao.getValorJurosMora());
        boletoDadosPagamento.setValorMulta(configuracao.getValorMulta());

        return boletoDadosPagamento;
    }

    private Date calcularDataBaseDias(Date dataVencimento, Long addDays) {

        if (LongUtil.isNullOrZero(addDays)) {
            return dataVencimento;
        }

        return DateUtil.addDays(dataVencimento, addDays);
    }

    private Titulo gerarTitulo(BancoBoletoService bancoGeracao, PagamentoBoletoVO pagamentoBoleto, TipoTituloEnum tipoTitulo, Aceite aceite,
            Sacado sacado, String nossoNumero, Date dataDocumento) {

        Titulo titulo = new Titulo(bancoGeracao.createContaBancaria(), sacado, bancoGeracao.gerarCedente());
        titulo.setNumeroDoDocumento(pagamentoBoleto.getNumeroDocumento().toString());
        titulo.setDataDoVencimento(pagamentoBoleto.getDataVencimento());
        titulo.setValor(pagamentoBoleto.getValorPagamento());
        titulo.setDataDoDocumento(dataDocumento);
        titulo.setAceite(aceite);
        titulo.setNossoNumero(nossoNumero.substring(0, 8));
        titulo.setDigitoDoNossoNumero(nossoNumero.substring(nossoNumero.length() - 1, nossoNumero.length()));
        titulo.setTipoDeDocumento(TipoDeTitulo.valueOf(tipoTitulo.getCodigo()));

        return titulo;
    }

    private void gerarInstrucoesSacado(Boleto boleto, Map<Integer, String> instrucoes) {

        Class<?> clazz = boleto.getClass();

        for (Entry<Integer, String> entry : instrucoes.entrySet()) {

            try {

                clazz.getDeclaredField("instrucao" + entry.getKey()).set(boleto, entry.getValue());

            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {

                throw new SysDescException(MensagemConstants.NUMERO_INSTRUCAO_PAGAMENTO_INVALIDA);
            }
        }

    }

    private Sacado gerarSacado(BoletoDadosCliente cliente) {

        Sacado sacado = new Sacado(cliente.getNome(), cliente.getCgc());

        Endereco enderecoSac = new Endereco();
        enderecoSac.setUF(UnidadeFederativa.valueOfSigla(cliente.getUF()));
        enderecoSac.setLocalidade(cliente.getCidade());
        enderecoSac.setCep(new CEP(cliente.getCep()));
        enderecoSac.setBairro(cliente.getBairro());
        enderecoSac.setLogradouro(cliente.getEndereco());
        enderecoSac.setNumero(cliente.getNumero());

        sacado.addEndereco(enderecoSac);

        return sacado;
    }

}
