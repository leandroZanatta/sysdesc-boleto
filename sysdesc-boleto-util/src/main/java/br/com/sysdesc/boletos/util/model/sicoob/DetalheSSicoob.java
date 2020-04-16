package br.com.sysdesc.boletos.util.model.sicoob;

import br.com.sysdesc.boletos.util.anotations.XmlAnotation;
import br.com.sysdesc.boletos.util.model.InnerRecordModel;
import lombok.Data;

@Data
public class DetalheSSicoob implements InnerRecordModel {

	@XmlAnotation(name = "numeroLote", type = Long.class, size = 4)
	private Long numeroLote;

	@XmlAnotation(name = "numeroRegistro", type = Long.class, size = 5)
	private Long numeroRegistro;

	@XmlAnotation(name = "codigoMovimento", type = Long.class, size = 2)
	private Long codigoMovimento;

	@XmlAnotation(name = "informacao5", type = String.class, size = 40)
	private String informacao5;

	@XmlAnotation(name = "informacao6", type = String.class, size = 40)
	private String informacao6;

	@XmlAnotation(name = "informacao7", type = String.class, size = 40)
	private String informacao7;

	@XmlAnotation(name = "informacao8", type = String.class, size = 40)
	private String informacao8;

	@XmlAnotation(name = "informacao9", type = String.class, size = 40)
	private String informacao9;

	@Override
	public String getRecordName() {

		return "DetalheSegmentoS";
	}

}
