package br.com.sysdesc.boletos.util.model.sicoob;

import br.com.sysdesc.boletos.util.anotations.XmlAnotation;
import br.com.sysdesc.boletos.util.model.Model;
import lombok.Data;

@Data
public class AgenciaSicoob implements Model {

	@XmlAnotation(name = "agencia", type = Long.class, size = 5)
	private Long agencia;

	@XmlAnotation(name = "dvAgencia", type = String.class, size = 1)
	private String dvAgencia;

	@XmlAnotation(name = "conta", type = Long.class, size = 12)
	private Long conta;

	@XmlAnotation(name = "dvConta", type = String.class, size = 1)
	private String dvConta;

}
