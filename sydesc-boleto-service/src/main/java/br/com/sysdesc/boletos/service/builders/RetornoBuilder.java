package br.com.sysdesc.boletos.service.builders;

import br.com.sysdesc.arquivos.exceptions.SysdescArquivosException;
import br.com.sysdesc.boletos.service.retorno.listener.RetonoRemessaBoletoAutorizadoActionListener;
import br.com.sysdesc.boletos.service.retorno.listener.RetonoRemessaBoletoPagoActionListener;

public interface RetornoBuilder {

	public void build() throws SysdescArquivosException;

	public void addRetornoRemessaAutorizadoListener(RetonoRemessaBoletoAutorizadoActionListener retornoActionListener);

	public void addRetornoRemessaPagoListener(RetonoRemessaBoletoPagoActionListener retornoActionListener);
}
