package br.com.sysdesc.boletos.service.retorno.listener;

import br.com.sysdesc.boletos.repository.model.Boleto;

public interface RetonoRemessaBoletoAutorizadoActionListener extends RetornoActionListener {

	public void fireEvent(Boleto boleto);
}
