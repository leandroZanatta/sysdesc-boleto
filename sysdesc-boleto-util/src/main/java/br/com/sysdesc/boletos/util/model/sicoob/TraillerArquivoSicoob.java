package br.com.sysdesc.boletos.util.model.sicoob;

import br.com.sysdesc.boletos.util.anotations.XmlAnotation;
import br.com.sysdesc.boletos.util.model.RecordModel;
import lombok.Data;

@Data
public class TraillerArquivoSicoob implements RecordModel {

	@XmlAnotation(name = "quantidadeLotes", type = Long.class, size = 6)
	private Long quantidadeLotes;

	@XmlAnotation(name = "quantidadeRegistros", type = Long.class, size = 6)
	private Long quantidadeRegistros;

	@Override
	public String getRecordName() {
		return "TraillerArquivo";
	}

}
