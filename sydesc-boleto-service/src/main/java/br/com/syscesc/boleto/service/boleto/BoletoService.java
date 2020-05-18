package br.com.syscesc.boleto.service.boleto;

import static br.com.sysdesc.boletos.util.InstrucoesSacadoUtil.montarJurosMora;
import static br.com.sysdesc.boletos.util.InstrucoesSacadoUtil.montarMulta;
import static br.com.sysdesc.boletos.util.InstrucoesSacadoUtil.montarNaoRecebimento;
import static br.com.sysdesc.boletos.util.InstrucoesSacadoUtil.montarProtesto;
import static br.com.sysdesc.util.classes.DateUtil.addDays;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import br.com.sysdesc.boletos.util.vo.PagamentoBoletoVO;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.classes.LongUtil;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.enumeradores.TipoEnvioEmailEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusBoletoEnum;
import br.com.sysdesc.util.enumeradores.TipoTituloEnum;

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
        String agencia = StringUtil.formatarNumero(configuracao.getNumeroAgencia());
        String conta = StringUtil.formatarNumero(configuracao.getNumeroConta());

        Integer numeroAgencia = Integer.valueOf(agencia.substring(0, agencia.length() - 1));
        Integer numeroConta = Integer.valueOf(conta.substring(0, conta.length() - 1));

        String digitoAgencia = agencia.substring(agencia.length() - 1, agencia.length());
        String digitoConta = conta.substring(conta.length() - 1, conta.length());

        Date dataDocumento = new Date();

        Titulo titulo = this.gerarTitulo(bancoGeracao, pagamentoBoleto, tipoTitulo, aceite, this.gerarSacado(cliente), nossoNumero, dataDocumento);

        String nossoNumeroDV = nossoNumero.substring(nossoNumero.length() - 1, nossoNumero.length());

        Date diaMaximoPagamento = addDays(pagamentoBoleto.getDataVencimento(), configuracao.getDiasMaximoPagamento());
        Date diaProtesto = addDays(pagamentoBoleto.getDataVencimento(), configuracao.getDiasProtesto());

        BigDecimal valor = pagamentoBoleto.getValorPagamento();

        Boleto boleto = new Boleto(titulo);
        boleto.setLocalPagamento("Pagável  em qualquer Banco até o Vencimento.");
        boleto.setInstrucaoAoSacado("");
        boleto.addTextosExtras("txtRsNossoNumero", nossoNumero.substring(0, 7) + "-" + nossoNumeroDV);
        boleto.addTextosExtras("txtFcNossoNumero", nossoNumero.substring(0, 7) + "-" + nossoNumeroDV);
        boleto.addTextosExtras("txtRsAgenciaCodigoCedente", numeroAgencia + "-" + digitoAgencia + " / " + numeroConta + "-" + digitoConta);
        boleto.addTextosExtras("txtFcAgenciaCodigoCedente", numeroAgencia + "-" + digitoAgencia + " / " + numeroConta + "-" + digitoConta);

        boleto.setInstrucao1(montarNaoRecebimento(diaMaximoPagamento));
        boleto.setInstrucao2(montarJurosMora(configuracao.getCodigoJurosMora(), valor, configuracao.getValorJurosMora()));
        boleto.setInstrucao3(montarMulta(configuracao.getCodigoMulta(), valor, configuracao.getValorMulta()));
        boleto.setInstrucao4(montarProtesto(configuracao.getCodigoProtesto(), diaProtesto));

        byte[] arquivoBoleto = new BoletoViewer(boleto).getPdfAsByteArray();

        bancoBoletoFactory.atualizarNossoNumero(configuracao);

        br.com.sysdesc.boleto.repository.model.Boleto modeloBoleto = new br.com.sysdesc.boleto.repository.model.Boleto();

        modeloBoleto.setArquivo(arquivoBoleto);
        modeloBoleto.setConfiguracaoBoleto(configuracao);
        modeloBoleto.setNumeroBanco(banco.getNumeroBanco());
        modeloBoleto.setDataCadastro(dataDocumento);
        modeloBoleto.setDataVencimento(pagamentoBoleto.getDataVencimento());
        modeloBoleto.setNossoNumero(nossoNumero);
        modeloBoleto.setCodigoBarras(StringUtil.formatarNumero(boleto.getLinhaDigitavel().write()));
        modeloBoleto.setValorBoleto(pagamentoBoleto.getValorPagamento());
        modeloBoleto.setCodigoStatus(TipoStatusBoletoEnum.GERADO.getCodigo());
        modeloBoleto.setBoletoDadosCliente(cliente);
        modeloBoleto.setAceite(aceite.name());
        modeloBoleto.setEspecieTitulo(tipoTitulo.getCodigo().longValue());
        modeloBoleto.setBoletoDadosSacadorAvalista(avalista);
        modeloBoleto.setBoletoDadosPagamento(this.montarDadosPagamento(configuracao, diaMaximoPagamento, pagamentoBoleto.getDataVencimento()));
        modeloBoleto.setCodigoStatusEmail(TipoEnvioEmailEnum.AGUARDANDO.getCodigo());

        if (modeloBoleto.getBoletoDadosSacadorAvalista() != null) {
            modeloBoleto.getBoletoDadosSacadorAvalista().setBoleto(modeloBoleto);
        }

        modeloBoleto.getBoletoDadosCliente().setBoleto(modeloBoleto);
        modeloBoleto.getBoletoDadosPagamento().setBoleto(modeloBoleto);

        boletoDAO.salvar(modeloBoleto);

        return modeloBoleto;
    }

    private BoletoDadosPagamento montarDadosPagamento(ConfiguracaoBoleto configuracao, Date diaMaximoPagamento, Date dataVencimento) {

        Long codigoProtesto = configuracao.getCodigoProtesto();

        BoletoDadosPagamento boletoDadosPagamento = new BoletoDadosPagamento();
        boletoDadosPagamento.setCodigoCarteira(configuracao.getCodigoCarteira());
        boletoDadosPagamento.setModalidade(configuracao.getModalidade());
        boletoDadosPagamento.setCodigoProtesto(codigoProtesto);
        boletoDadosPagamento.setCodigoJurosMora(configuracao.getCodigoJurosMora());
        boletoDadosPagamento.setCodigoMulta(configuracao.getCodigoMulta());

        boletoDadosPagamento.setDiasProtesto(codigoProtesto.equals(DIAS_CORRIDOS) ? configuracao.getDiasProtesto() : 0L);
        boletoDadosPagamento.setDataLimitePagamento(diaMaximoPagamento);

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

    public void salvar(List<br.com.sysdesc.boleto.repository.model.Boleto> boletos) {

        boletoDAO.salvar(boletos);
    }

}
