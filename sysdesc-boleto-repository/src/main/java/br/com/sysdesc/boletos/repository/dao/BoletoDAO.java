package br.com.sysdesc.boletos.repository.dao;

import static br.com.sysdesc.boleto.repository.model.QBoleto.boleto;
import static br.com.sysdesc.boleto.repository.model.QRemessaBoleto.remessaBoleto;

import java.util.List;

import br.com.sysdesc.boletos.repository.model.Boleto;
import br.com.sysdesc.util.dao.AbstractGenericDAO;

public class BoletoDAO extends AbstractGenericDAO<Boleto> {

	private static final long serialVersionUID = 1L;

	public BoletoDAO() {
		super(boleto, boleto.idBoleto);
	}

	public List<Boleto> buscarBoletosParaEnvio(Long numeroBanco) {

		return from().where(boleto.numeroBanco.eq(numeroBanco)
				.and(subQuery().from(remessaBoleto).where(boleto.idBoleto.eq(remessaBoleto.id.codigoBoleto)).notExists())).list(boleto);
	}

	public Boleto buscarBoletosPorNossoNumero(Long numeroBanco, String numeroAgencia, String numeroConta, String nossoNumero) {

		return from().where(boleto.numeroBanco.eq(numeroBanco)
				.and(boleto.nossoNumero.eq(nossoNumero)
						.and(boleto.configuracaoBoleto.numeroAgencia.eq(numeroAgencia)
								.and(boleto.configuracaoBoleto.banco.numeroConta.eq(numeroConta)))))
				.singleResult(boleto);
	}

}