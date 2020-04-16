package br.com.sysdesc.boletos.util.model.sicoob;

import java.math.BigDecimal;

import br.com.sysdesc.boletos.util.anotations.XmlAnotation;
import br.com.sysdesc.boletos.util.model.InnerRecordModel;
import lombok.Data;

@Data
public class DetalheRSicoob implements InnerRecordModel {

	@XmlAnotation(name = "numeroLote", type = Long.class, size = 4)
	private Long numeroLote;

	@XmlAnotation(name = "numeroRegistro", type = Long.class, size = 5)
	private Long numeroRegistro;

	@XmlAnotation(name = "codigoMovimento", type = Long.class, size = 2)
	private Long codigoMovimento;

	@XmlAnotation(name = "codigoDesconto2", type = Long.class, size = 1)
	private Long codigoDesconto2;

	@XmlAnotation(name = "dataDesconto2", type = String.class, size = 8)
	private String dataDesconto2;

	@XmlAnotation(name = "valorDesconto2", type = BigDecimal.class, decimal = 2, size = 15)
	private BigDecimal valorDesconto2;

	@XmlAnotation(name = "codigoDesconto3", type = Long.class, size = 1)
	private Long codigoDesconto3;

	@XmlAnotation(name = "dataDesconto3", type = String.class, size = 8)
	private String dataDesconto3;

	@XmlAnotation(name = "valorDesconto3", type = BigDecimal.class, decimal = 2, size = 15)
	private BigDecimal valorDesconto3;

	@XmlAnotation(name = "codigoMulta", type = Long.class, size = 1)
	private Long codigoMulta;

	@XmlAnotation(name = "dataMulta", type = String.class, size = 8)
	private String dataMulta;

	@XmlAnotation(name = "valorMulta", type = BigDecimal.class, decimal = 2, size = 15)
	private BigDecimal valorMulta;

	@XmlAnotation(name = "dataLimitePagamento", type = String.class, size = 8)
	private String dataLimitePagamento;

	@Override
	public String getRecordName() {

		return "DetalheSegmentoR";
	}

}
