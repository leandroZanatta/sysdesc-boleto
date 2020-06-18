package br.com.boletos.service.sicoob.retorno.models;

import br.com.sysdesc.arquivos.metamodel.Field;
import br.com.sysdesc.arquivos.metamodel.IgnoreId;
import lombok.Data;

@Data
@IgnoreId(type = String.class, name = "tipoRegistro", start = 7, end = 8, defaultValue = "9")
public class RetornoSicoobTraillerLoteModel {

	@Field(order = 1, start = 17, end = 23)
	private Long quantidadeLotes;

}
