package br.com.boletos.service.sicoob.retorno.models;

import br.com.sysdesc.arquivos.metamodel.GroupOfRecords;
import br.com.sysdesc.arquivos.metamodel.Record;
import lombok.Data;

@Data
@GroupOfRecords(name = "detalheRetorno")
public class RetornoSicoobDetalheModel {

	@Record(order = 0)
	private RetornoSicoobDetalheSegmentoTModel retornoSicoobDetalheSegmentoTModel;

	@Record(order = 1)
	private RetornoSicoobDetalheSegmentoUModel retornoSicoobDetalheSegmentoUModel;

}
