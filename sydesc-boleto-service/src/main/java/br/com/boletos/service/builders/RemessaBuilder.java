package br.com.boletos.service.builders;

import java.util.List;

import org.jrimum.texgit.FlatFile;
import org.jrimum.texgit.Record;

import br.com.sysdesc.boleto.repository.model.Boleto;

public interface RemessaBuilder {

	public FlatFile<Record> build(List<Boleto> boletosParaEnviar);

}
