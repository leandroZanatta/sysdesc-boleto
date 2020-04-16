package br.com.sysdesc.boletos.util.model.sicoob;

import br.com.sysdesc.boletos.util.anotations.XmlAnotation;
import br.com.sysdesc.boletos.util.model.Model;
import lombok.Data;

@Data
public class CedenteSicoob implements Model {

	@XmlAnotation(name = "tipoCliente", type = String.class, size = 1)
	private String tipoCliente;

	@XmlAnotation(name = "cgcCliente", type = Long.class, size = 15)
	private Long cgc;

	@XmlAnotation(name = "nomeEmpresa", type = String.class, size = 30)
	private String nome;
}
