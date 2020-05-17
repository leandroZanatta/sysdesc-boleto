package br.com.boletos.service.sicoob.models;

import static br.com.sysdesc.boletos.util.InstrucoesSacadoUtil.montarJurosMora;
import static br.com.sysdesc.boletos.util.InstrucoesSacadoUtil.montarMulta;
import static br.com.sysdesc.boletos.util.InstrucoesSacadoUtil.montarNaoRecebimento;
import static br.com.sysdesc.boletos.util.InstrucoesSacadoUtil.montarProtesto;
import static br.com.sysdesc.util.classes.DateUtil.addDays;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jrimum.texgit.FlatFile;
import org.jrimum.texgit.Record;

import br.com.sysdesc.boleto.repository.model.Boleto;
import br.com.sysdesc.boleto.repository.model.BoletoDadosCliente;
import br.com.sysdesc.boleto.repository.model.BoletoDadosPagamento;
import br.com.sysdesc.boleto.repository.model.BoletoDadosSacadorAvalista;
import br.com.sysdesc.boletos.util.RecordUtil;
import br.com.sysdesc.boletos.util.model.sicoob.AgenciaSicoob;
import br.com.sysdesc.boletos.util.model.sicoob.DetalhePSicoob;
import br.com.sysdesc.boletos.util.model.sicoob.DetalheQSicoob;
import br.com.sysdesc.boletos.util.model.sicoob.DetalheRSicoob;
import br.com.sysdesc.boletos.util.model.sicoob.DetalheSSicoob;
import br.com.sysdesc.util.classes.ContadorUtil;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.enumeradores.TipoClienteEnum;
import lombok.Data;

@Data
public class DetalheSicoob {

    private static final long A4_SEM_ENVELOPAMENTO = 4L;

    private static final long ENTRADA_TITULOS = 1L;

    private final DetalhePSicoob detalhePSicoob = new DetalhePSicoob();

    private final DetalheQSicoob detalheQSicoob = new DetalheQSicoob();

    private final DetalheRSicoob detalheRSicoob = new DetalheRSicoob();

    private final DetalheSSicoob detalheSSicoob = new DetalheSSicoob();

    public DetalheSicoob(Long numeroLote, ContadorUtil contadorUtil, AgenciaSicoob agencia, Boleto boleto) {

        BoletoDadosPagamento boletoDadosPagamento = boleto.getBoletoDadosPagamento();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMYYYY");

        detalhePSicoob.setNumeroRegistro(contadorUtil.next());
        detalheQSicoob.setNumeroRegistro(contadorUtil.next());
        detalheRSicoob.setNumeroRegistro(contadorUtil.next());
        detalheSSicoob.setNumeroRegistro(contadorUtil.next());

        detalheQSicoob.setNumeroLote(numeroLote);
        detalheRSicoob.setNumeroLote(numeroLote);
        detalhePSicoob.setNumeroLote(numeroLote);
        detalheSSicoob.setNumeroLote(numeroLote);

        detalhePSicoob.setCodigoMovimento(ENTRADA_TITULOS);
        detalheQSicoob.setCodigoMovimento(ENTRADA_TITULOS);
        detalheRSicoob.setCodigoMovimento(ENTRADA_TITULOS);
        detalheSSicoob.setCodigoMovimento(ENTRADA_TITULOS);

        detalhePSicoob.setAgencia(agencia);
        detalhePSicoob.setDataEmissao(simpleDateFormat.format(boleto.getDataCadastro()));
        detalhePSicoob.setVencimento(simpleDateFormat.format(boleto.getDataVencimento()));
        detalhePSicoob.setValorNominal(boleto.getValorBoleto());
        detalhePSicoob.setNossoNumero(Long.valueOf(boleto.getNossoNumero()));

        detalhePSicoob.setAceite(boleto.getAceite());
        detalhePSicoob.setEspecieTitulo(boleto.getEspecieTitulo());

        detalhePSicoob.setCodigoParaProtesto(boletoDadosPagamento.getCodigoProtesto());
        detalhePSicoob.setPrazoProtesto(boletoDadosPagamento.getDiasProtesto());
        detalhePSicoob.setModalidade(boletoDadosPagamento.getModalidade());
        detalhePSicoob.setCarteira(boletoDadosPagamento.getCodigoCarteira());
        detalhePSicoob.setCodigoJurosMora(boletoDadosPagamento.getCodigoJurosMora());
        detalhePSicoob.setDataJurosMora(simpleDateFormat.format(boletoDadosPagamento.getDataJurosMora()));
        detalhePSicoob.setValorJurosMora(boletoDadosPagamento.getValorJurosMora());

        detalhePSicoob.setIdentificacaoTitulo(boletoDadosPagamento.getBoleto().getIdBoleto().toString());
        detalhePSicoob.setNumeroDocumento(boletoDadosPagamento.getBoleto().getIdBoleto().toString());

        detalhePSicoob.setTipoFormulario(A4_SEM_ENVELOPAMENTO);
        detalhePSicoob.setNumeroParcela(1L);
        detalhePSicoob.setCodigoDesconto(0L);//Isento
        detalhePSicoob.setDataDesconto("");
        detalhePSicoob.setValorDesconto(BigDecimal.ZERO);

        //Numero do contrato para operação de crédito(adicionar nas configurações do boleto) 0 - Simples sem registro 
        detalhePSicoob.setNumeroContrato(0L);
        detalhePSicoob.setValorAbatimento(BigDecimal.ZERO);
        detalhePSicoob.setValorIof(BigDecimal.ZERO);

        this.gerarDetalheQ(boleto);
        this.gerarDetalheR(boletoDadosPagamento, simpleDateFormat);
        this.gerarDetalheS(boleto);

        detalhePSicoob.setDetalheQSicoob(detalheQSicoob);
        detalhePSicoob.setDetalheRSicoob(detalheRSicoob);
        detalhePSicoob.setDetalheSSicoob(detalheSSicoob);

    }

    private void gerarDetalheS(Boleto boleto) {

        BoletoDadosPagamento pagamento = boleto.getBoletoDadosPagamento();

        Date diaProtesto = addDays(boleto.getDataVencimento(), pagamento.getDiasProtesto());

        detalheSSicoob.setInformacao5(montarNaoRecebimento(pagamento.getDataLimitePagamento()));
        detalheSSicoob.setInformacao6(montarJurosMora(pagamento.getCodigoJurosMora(), boleto.getValorBoleto(), pagamento.getValorJurosMora()));
        detalheSSicoob.setInformacao7(montarMulta(pagamento.getCodigoMulta(), boleto.getValorBoleto(), pagamento.getValorMulta()));
        detalheSSicoob.setInformacao8(montarProtesto(pagamento.getCodigoProtesto(), diaProtesto));
        detalheSSicoob.setInformacao9("");
    }

    private void gerarDetalheR(BoletoDadosPagamento boletoDadosPagamento, SimpleDateFormat simpleDateFormat) {

        detalheRSicoob.setDataLimitePagamento(simpleDateFormat.format(boletoDadosPagamento.getDataLimitePagamento()));

        detalheRSicoob.setCodigoMulta(boletoDadosPagamento.getCodigoMulta());
        detalheRSicoob.setDataMulta(simpleDateFormat.format(boletoDadosPagamento.getDataMulta()));
        detalheRSicoob.setValorMulta(boletoDadosPagamento.getValorMulta());

        detalheRSicoob.setCodigoDesconto2(0L);
        detalheRSicoob.setCodigoDesconto3(0L);
        detalheRSicoob.setDataDesconto2("");
        detalheRSicoob.setDataDesconto3("");
        detalheRSicoob.setValorDesconto2(BigDecimal.ZERO);
        detalheRSicoob.setValorDesconto3(BigDecimal.ZERO);
    }

    private void gerarDetalheQ(Boleto boleto) {

        BoletoDadosCliente boletoDadosCliente = boleto.getBoletoDadosCliente();
        BoletoDadosSacadorAvalista dadosSacadorAvalista = boleto.getBoletoDadosSacadorAvalista();

        detalheQSicoob.setTipoCliente(getTipoCliente(boletoDadosCliente.getFlagTipoCliente()));
        detalheQSicoob.setCgcCliente(Long.valueOf(StringUtil.formatarNumero(boletoDadosCliente.getCgc())));
        detalheQSicoob.setNome(boletoDadosCliente.getNome());

        detalheQSicoob.setUf(boletoDadosCliente.getUF());
        detalheQSicoob.setCidade(boletoDadosCliente.getCidade());
        detalheQSicoob.setEndereco(boletoDadosCliente.getEndereco());
        detalheQSicoob.setBairro(boletoDadosCliente.getBairro());
        detalheQSicoob.setCep(Long.valueOf(boletoDadosCliente.getCep().substring(0, 5)));
        detalheQSicoob.setSufixoCep(Long.valueOf(boletoDadosCliente.getCep().substring(6, 9)));

        if (dadosSacadorAvalista != null) {

            detalheQSicoob.setTipoSacadorAvalista(getTipoCliente(dadosSacadorAvalista.getFlagTipoCliente()));
            detalheQSicoob.setNomeSacadorAvalista(dadosSacadorAvalista.getNome());
            detalheQSicoob.setCgcSacadorAvalista(Long.valueOf(StringUtil.formatarNumero(dadosSacadorAvalista.getCgc())));
        }

        detalheQSicoob.setBancoCorrespondente("");
        detalheQSicoob.setBancoCompensacao(0L);
    }

    private long getTipoCliente(String tipoCliente) {

        return TipoClienteEnum.findByCodigo(tipoCliente).equals(TipoClienteEnum.PESSOA_FISICA) ? 1L : 2L;
    }

    public void build(FlatFile<Record> records) {

        RecordUtil.createRecord(detalhePSicoob, records);
    }

}
