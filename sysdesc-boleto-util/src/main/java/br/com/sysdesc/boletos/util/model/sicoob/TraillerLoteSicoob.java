package br.com.sysdesc.boletos.util.model.sicoob;

import java.math.BigDecimal;

import br.com.sysdesc.boletos.util.anotations.XmlAnotation;
import br.com.sysdesc.boletos.util.model.RecordModel;
import lombok.Data;

@Data
public class TraillerLoteSicoob implements RecordModel {

	@XmlAnotation(name = "numeroLote", type = Long.class, size = 4)
	private Long numeroLote;

	@XmlAnotation(name = "quantidadeRegistros", type = Long.class, size = 4)
	private Long quantidadeRegistros;

	@XmlAnotation(name = "quantidadeTitulosCobrancaSimples", type = Long.class, size = 6)
	private Long quantidadeTitulosCobrancaSimples;

	@XmlAnotation(name = "quantidadeTitulosCobrancaVinculada", type = Long.class, size = 6)
	private Long quantidadeTitulosCobrancaVinculada;

	@XmlAnotation(name = "quantidadeTitulosCobrancaCaucionada", type = Long.class, size = 6)
	private Long quantidadeTitulosCobrancaCaucionada;

	@XmlAnotation(name = "quantidadeTitulosCobrancaDescontada", type = Long.class, size = 6)
	private Long quantidadeTitulosCobrancaDescontada;

	@XmlAnotation(name = "totalizacaoCobrancaSimples", type = BigDecimal.class, size = 15, decimal = 2)
	private BigDecimal totalizacaoCobrancaSimples;

	@XmlAnotation(name = "totalizacaoCobrancaVinculada", type = BigDecimal.class, size = 15, decimal = 2)
	private BigDecimal totalizacaoCobrancaVinculada;

	@XmlAnotation(name = "totalizacaoCobrancaCaucionada", type = BigDecimal.class, size = 15, decimal = 2)
	private BigDecimal totalizacaoCobrancaCaucionada;

	@XmlAnotation(name = "totalizacaoCobrancaDescontada", type = BigDecimal.class, size = 15, decimal = 2)
	private BigDecimal totalizacaoCobrancaDescontada;

	@Override
	public String getRecordName() {
		return "TraillerLote";
	}

}
