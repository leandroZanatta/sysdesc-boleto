package br.com.sysdesc.boletos.util.model.sicoob;

import java.math.BigDecimal;

import br.com.sysdesc.boletos.util.anotations.XmlAnotation;
import br.com.sysdesc.boletos.util.model.RecordModel;
import lombok.Data;

@Data
public class DetalhePSicoob implements RecordModel {

	private AgenciaSicoob agencia;

	private DetalheQSicoob detalheQSicoob;

	private DetalheRSicoob detalheRSicoob;

	private DetalheSSicoob detalheSSicoob;

	@XmlAnotation(name = "numeroLote", type = Long.class, size = 4)
	private Long numeroLote;

	@XmlAnotation(name = "numeroRegistro", type = Long.class, size = 5)
	private Long numeroRegistro;

	@XmlAnotation(name = "codigoMovimento", type = Long.class, size = 2)
	private Long codigoMovimento;

	@XmlAnotation(name = "modalidade", type = String.class, size = 2)
	private String modalidade;

	@XmlAnotation(name = "tipoFormulario", type = Long.class, size = 1)
	private Long tipoFormulario;

	@XmlAnotation(name = "nossoNumero", type = Long.class, size = 20)
	private Long nossoNumero;

	@XmlAnotation(name = "carteira", type = Long.class, size = 1)
	private Long carteira;

	@XmlAnotation(name = "numeroDocumento", type = String.class, size = 15)
	private String numeroDocumento;

	@XmlAnotation(name = "vencimento", type = String.class, size = 8)
	private String vencimento;

	@XmlAnotation(name = "valorNominal", type = BigDecimal.class, size = 15, decimal = 2)
	private BigDecimal valorNominal;

	@XmlAnotation(name = "especieTitulo", type = Long.class, size = 2)
	private Long especieTitulo;

	@XmlAnotation(name = "aceite", type = String.class, size = 1)
	private String aceite;

	@XmlAnotation(name = "dataEmissao", type = String.class, size = 8)
	private String dataEmissao;

	@XmlAnotation(name = "codigoJurosMora", type = Long.class, size = 1)
	private Long codigoJurosMora;

	@XmlAnotation(name = "dataJurosMora", type = String.class, size = 8)
	private String dataJurosMora;

	@XmlAnotation(name = "valorJurosMora", type = BigDecimal.class, size = 15, decimal = 2)
	private BigDecimal valorJurosMora;

	@XmlAnotation(name = "codigoDesconto", type = Long.class, size = 1)
	private Long codigoDesconto;

	@XmlAnotation(name = "numeroParcela", type = Long.class, size = 2)
	private Long numeroParcela;

	@XmlAnotation(name = "dataDesconto", type = String.class, size = 8, decimal = 2)
	private String dataDesconto;

	@XmlAnotation(name = "valorDesconto", type = BigDecimal.class, size = 15, decimal = 2)
	private BigDecimal valorDesconto;

	@XmlAnotation(name = "valorIof", type = BigDecimal.class, size = 15, decimal = 2)
	private BigDecimal valorIof;

	@XmlAnotation(name = "valorAbatimento", type = BigDecimal.class, size = 15, decimal = 2)
	private BigDecimal valorAbatimento;

	@XmlAnotation(name = "identificacaoTitulo", type = String.class, size = 25)
	private String identificacaoTitulo;

	@XmlAnotation(name = "codigoParaProtesto", type = Long.class, size = 1)
	private Long codigoParaProtesto;

	@XmlAnotation(name = "prazoProtesto", type = Long.class, size = 2)
	private Long prazoProtesto;

	@XmlAnotation(name = "numeroContrato", type = Long.class, size = 10)
	private Long numeroContrato;

	@Override
	public String getRecordName() {
		return "DetalheSegmentoP";
	}

}
