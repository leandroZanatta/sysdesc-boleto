package br.com.boletos.service.builders;

import br.com.sysdesc.arquivos.exceptions.SysdescArquivosException;
import br.com.sysdesc.boleto.repository.model.Retorno;

public interface RetornoBuilder {

	public Retorno build() throws SysdescArquivosException;
}
