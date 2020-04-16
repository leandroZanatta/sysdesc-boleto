package br.com.sysdesc.boleto.repository.dao;

import static br.com.sysdesc.boleto.repository.model.QBanco.banco;
import static br.com.sysdesc.boleto.repository.model.QConfiguracaoBoleto.configuracaoBoleto;

import java.util.List;

import br.com.sysdesc.boleto.repository.model.Banco;
import br.com.sysdesc.pesquisa.repository.dao.impl.PesquisableDAOImpl;

public class BancoDAO extends PesquisableDAOImpl<Banco> {

	private static final long serialVersionUID = 1L;

	public BancoDAO() {
		super(banco, banco.idBanco);
	}

	public List<Banco> buscarBancosSuporteBoleto() {

		return from().where(subQuery().from(configuracaoBoleto).where(configuracaoBoleto.banco.eq(banco)).exists())
				.list(banco);
	}

}
