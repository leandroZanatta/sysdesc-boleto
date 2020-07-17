package br.com.sysdesc.boletos.service.sicoob.remessa;

import java.io.File;
import java.util.List;

import org.jrimum.texgit.FlatFile;
import org.jrimum.texgit.Record;
import org.jrimum.texgit.Texgit;

import br.com.sysdesc.boletos.repository.model.Boleto;
import br.com.sysdesc.boletos.repository.model.ConfiguracaoBoleto;
import br.com.sysdesc.boletos.service.builders.RemessaBuilder;
import br.com.sysdesc.boletos.service.sicoob.remessa.models.RemessaSicoob;
import br.com.sysdesc.util.resources.Configuracoes;

public class RemessaSicoobBuilder implements RemessaBuilder {

	private final ConfiguracaoBoleto configuracaoBoleto;

	public RemessaSicoobBuilder(ConfiguracaoBoleto configuracaoBoleto) {
		this.configuracaoBoleto = configuracaoBoleto;
	}

	@Override
	public FlatFile<Record> build(List<Boleto> boletosParaEnviar, Long codigoRemessa) {

		FlatFile<Record> records = Texgit.createFlatFile(new File(Configuracoes.FOLDER_RESOURCES + "/schemas/sicoob/remessa_sicoob_240.txg.xml"));

		Long numeroRemessaLote = 1L;

		RemessaSicoob remessaSicoob = new RemessaSicoob(codigoRemessa, configuracaoBoleto);

		remessaSicoob.criarLote(numeroRemessaLote);

		boletosParaEnviar.forEach(remessaSicoob::criarDetalhe);

		remessaSicoob.build(records);

		return records;

	}

}
