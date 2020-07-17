package br.com.sysdesc.boletos.service.retorno.listener;

import br.com.sysdesc.boletos.repository.model.Boleto;
import br.com.sysdesc.boletos.service.retorno.models.RetornoDetalheModel;

public interface RetonoRemessaBoletoPagoActionListener extends RetornoActionListener {

	public abstract void fireEvent(Boleto boleto, RetornoDetalheModel boletoDetalhe);
}
