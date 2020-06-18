package br.com.boletos.service.sicoob.retorno.models;

import java.util.List;

import br.com.sysdesc.arquivos.metamodel.GroupOfRecords;
import br.com.sysdesc.arquivos.metamodel.Record;
import br.com.sysdesc.arquivos.metamodel.RootFile;
import lombok.Data;

@RootFile
@GroupOfRecords(name = "arquivoRemessa")
@Data
public class RetornoSicoobModel {

	@Record(order = 0)
	private RetornoSicoobHeaderArquivoModel headerArquivo;

	@Record(order = 1)
	private RetornoSicoobHeaderLoteModel headerLote;

	@Record(order = 2)
	private List<RetornoSicoobDetalheModel> detalhe;

	@Record(order = 3)
	private RetornoSicoobTraillerLoteModel traillerLote;

	@Record(order = 4)
	private RetornoSicoobTraillerArquivoModel traillerArquivo;

}
