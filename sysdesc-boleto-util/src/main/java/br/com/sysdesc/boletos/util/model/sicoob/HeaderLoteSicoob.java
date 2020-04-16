package br.com.sysdesc.boletos.util.model.sicoob;

import br.com.sysdesc.boletos.util.anotations.XmlAnotation;
import br.com.sysdesc.boletos.util.model.RecordModel;
import lombok.Data;

@Data
public class HeaderLoteSicoob implements RecordModel {

	private CedenteSicoob cedente;

	private AgenciaSicoob agencia;

	@XmlAnotation(name = "numeroLote", type = Long.class, size = 4)
	private Long numeroLote;

	@XmlAnotation(name = "numeroRemessa", type = Long.class, size = 6)
	private Long numeroRemessa;

	@XmlAnotation(name = "dataGeracao", type = String.class, size = 8)
	private String dataGeracao;

	@Override
	public String getRecordName() {
		return "HeaderLote";
	}

}
