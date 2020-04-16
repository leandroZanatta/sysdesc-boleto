package br.com.sysdesc.boleto.repository.dao;

import static br.com.sysdesc.boleto.repository.model.QConfiguracaoBoleto.configuracaoBoleto;

import br.com.sysdesc.boleto.repository.model.ConfiguracaoBoleto;
import br.com.sysdesc.pesquisa.repository.dao.impl.PesquisableDAOImpl;

public class ConfiguracaoBoletoDAO extends PesquisableDAOImpl<ConfiguracaoBoleto> {

	private static final long serialVersionUID = 1L;

	public ConfiguracaoBoletoDAO() {
		super(configuracaoBoleto, configuracaoBoleto.idConfiguracaoBoleto);
	}

	public ConfiguracaoBoleto buscarConfiguracao(Long codigoBanco) {

		return from().where(configuracaoBoleto.banco.idBanco.eq(codigoBanco)).singleResult(configuracaoBoleto);
	}
}
