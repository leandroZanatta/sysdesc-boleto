package br.com.sysdesc.boletos.util.model.sicoob;

import br.com.sysdesc.boletos.util.anotations.XmlAnotation;
import br.com.sysdesc.boletos.util.model.RecordModel;
import lombok.Data;

@Data
public class HeaderArquivoSicoob implements RecordModel {

	private CedenteSicoob cedente;

	private AgenciaSicoob agencia;

	@XmlAnotation(name = "dataGeracao", type = String.class, size = 8)
	private String dataGeracao;

	@XmlAnotation(name = "horaGeracao", type = String.class, size = 6)
	private String horaGeracao;

	@XmlAnotation(name = "codigoRemessa", type = Long.class, size = 6)
	private Long codigoRemessa;

	@Override
	public String getRecordName() {
		return "Header";
	}

}
