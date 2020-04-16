package br.com.sysdesc.boleto.repository.dao;

import static br.com.sysdesc.boleto.repository.model.QBoleto.boleto;

import java.util.List;

import br.com.sysdesc.boleto.repository.model.Boleto;
import br.com.sysdesc.util.dao.AbstractGenericDAO;
import br.com.sysdesc.util.enumeradores.TipoStatusBoletoEnum;

public class BoletoDAO extends AbstractGenericDAO<Boleto> {

	private static final long serialVersionUID = 1L;

	public BoletoDAO() {
		super(boleto, boleto.idBoleto);
	}

	public List<Boleto> buscarBoletosParaEnvio(Long numeroBanco) {

		return from().where(
				boleto.numeroBanco.eq(numeroBanco).and(boleto.codigoStatus.eq(TipoStatusBoletoEnum.GERADO.getCodigo())))
				.list(boleto);
	}

}
