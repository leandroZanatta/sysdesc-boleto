package br.com.boletos.service.sicoob.models;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jrimum.texgit.FlatFile;
import org.jrimum.texgit.Record;

import br.com.sysdesc.boleto.repository.model.Boleto;
import br.com.sysdesc.boletos.util.RecordUtil;
import br.com.sysdesc.boletos.util.model.sicoob.AgenciaSicoob;
import br.com.sysdesc.boletos.util.model.sicoob.DetalhePSicoob;
import br.com.sysdesc.boletos.util.model.sicoob.DetalheQSicoob;
import br.com.sysdesc.boletos.util.model.sicoob.DetalheRSicoob;
import br.com.sysdesc.boletos.util.model.sicoob.DetalheSSicoob;
import br.com.sysdesc.util.classes.ContadorUtil;
import lombok.Data;

@Data
public class DetalheSicoob {

	private final DetalhePSicoob detalhePSicoob = new DetalhePSicoob();

	private final DetalheQSicoob detalheQSicoob = new DetalheQSicoob();

	private final DetalheRSicoob detalheRSicoob = new DetalheRSicoob();

	private final DetalheSSicoob detalheSSicoob = new DetalheSSicoob();

	public DetalheSicoob(ContadorUtil contadorUtil, AgenciaSicoob agencia, Boleto boleto) {

		detalhePSicoob.setNumeroRegistro(contadorUtil.next());
		detalheQSicoob.setNumeroRegistro(contadorUtil.next());
		detalheRSicoob.setNumeroRegistro(contadorUtil.next());
		detalheSSicoob.setNumeroRegistro(contadorUtil.next());

		detalhePSicoob.setAgencia(agencia);
		detalhePSicoob.setAceite("N");
		detalhePSicoob.setModalidade("01");
		detalhePSicoob.setIdentificacaoTitulo("");
		detalhePSicoob.setNumeroDocumento("DUPLICATA 01");
		detalhePSicoob.setVencimento(new SimpleDateFormat("ddMMYYYY").format(new Date()));
		detalhePSicoob.setDataDesconto(new SimpleDateFormat("ddMMYYYY").format(new Date()));
		detalhePSicoob.setDataJurosMora(new SimpleDateFormat("ddMMYYYY").format(new Date()));
		detalhePSicoob.setDataEmissao(new SimpleDateFormat("ddMMYYYY").format(new Date()));
		detalhePSicoob.setCarteira(1L);
		detalhePSicoob.setTipoFormulario(4L);
		detalhePSicoob.setCodigoDesconto(1L);
		detalhePSicoob.setNumeroParcela(1L);
		detalhePSicoob.setCodigoJurosMora(2L);
		detalhePSicoob.setCodigoMovimento(1L);
		detalhePSicoob.setCodigoParaProtesto(1L);
		detalhePSicoob.setEspecieTitulo(2L);
		detalhePSicoob.setNossoNumero(1L);
		detalhePSicoob.setNumeroContrato(1980L);
		detalhePSicoob.setNumeroLote(1L);
		detalhePSicoob.setValorAbatimento(BigDecimal.valueOf(0.0));
		detalhePSicoob.setValorDesconto(BigDecimal.valueOf(0.1));
		detalhePSicoob.setValorIof(BigDecimal.valueOf(0.0));
		detalhePSicoob.setValorJurosMora(BigDecimal.valueOf(0.11));
		detalhePSicoob.setValorNominal(BigDecimal.valueOf(18.4));
		detalhePSicoob.setPrazoProtesto(0L);

		detalheQSicoob.setBairro("Centro");
		detalheQSicoob.setBancoCorrespondente("");
		detalheQSicoob.setBancoCompensacao(0L);
		detalheQSicoob.setCep(89910L);
		detalheQSicoob.setCgcCliente(8783466940L);
		detalheQSicoob.setCidade("Descanso");
		detalheQSicoob.setEndereco("Rua Padre Francisco massure");
		detalheQSicoob.setNome("Leandro Zanatta");
		detalheQSicoob.setCodigoSacadorAvalista(0L);
		detalheQSicoob.setNomeSacadorAvalista("");
		detalheQSicoob.setCgcSacadorAvalista(0L);
		detalheQSicoob.setSufixoCep(0L);
		detalheQSicoob.setTipoCliente(1L);
		detalheQSicoob.setUf("SC");
		detalheQSicoob.setCodigoMovimento(1L);
		detalheQSicoob.setNumeroLote(1L);

		detalheRSicoob.setCodigoDesconto2(0L);
		detalheRSicoob.setCodigoDesconto3(0L);
		detalheRSicoob.setCodigoMulta(0L);
		detalheRSicoob.setDataDesconto2("");
		detalheRSicoob.setDataDesconto3("");
		detalheRSicoob.setDataLimitePagamento(new SimpleDateFormat("ddMMYYYY").format(new Date()));
		detalheRSicoob.setDataMulta("");
		detalheRSicoob.setValorDesconto2(BigDecimal.ZERO);
		detalheRSicoob.setValorDesconto3(BigDecimal.ZERO);
		detalheRSicoob.setValorMulta(BigDecimal.ZERO);
		detalheRSicoob.setCodigoMovimento(1L);
		detalheRSicoob.setNumeroLote(1L);

		detalheSSicoob.setCodigoMovimento(1L);
		detalheSSicoob.setNumeroLote(1L);
		detalheSSicoob.setInformacao5("APÓS VENCIMENTO MULTA DE XX");
		detalheSSicoob.setInformacao6("APÓS VENCIMENTO, MORA DE XX");
		detalheSSicoob.setInformacao7("APENAS TESTE.");
		detalheSSicoob.setInformacao8("");
		detalheSSicoob.setInformacao9("");

		detalhePSicoob.setDetalheQSicoob(detalheQSicoob);
		detalhePSicoob.setDetalheRSicoob(detalheRSicoob);
		detalhePSicoob.setDetalheSSicoob(detalheSSicoob);

	}

	public void build(FlatFile<Record> records) {

		RecordUtil.createRecord(detalhePSicoob, records);
	}

}
