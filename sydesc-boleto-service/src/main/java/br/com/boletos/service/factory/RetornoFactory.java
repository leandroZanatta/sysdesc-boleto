package br.com.boletos.service.factory;

import java.util.List;

import br.com.boletos.service.builders.RetornoBuilder;
import br.com.boletos.service.sicoob.retorno.RetornoSicoobBuilder;
import br.com.sysdesc.boleto.repository.dao.ConfiguracaoBoletoDAO;
import br.com.sysdesc.boleto.repository.model.ConfiguracaoBoleto;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.enumeradores.TipoBancoEnum;
import br.com.sysdesc.util.exception.SysDescException;

public class RetornoFactory {

	private final ConfiguracaoBoletoDAO configuracaoBoletoDAO = new ConfiguracaoBoletoDAO();

	public RetornoBuilder getRetornoBuilder(List<String> records) {

		if (records.get(0).substring(0, 3).equals(TipoBancoEnum.BANCO_SICOOB.getCodigo().toString())) {
			return new RetornoSicoobBuilder(records);
		}

		throw new SysDescException(MensagemConstants.BANCO_NAO_SUPORTADO_BOLETO);
	}

	public ConfiguracaoBoleto buscarConfiguracaoBoleto(Long codigoBanco) {

		ConfiguracaoBoleto configuracaoBoleto = configuracaoBoletoDAO.buscarConfiguracao(codigoBanco);

		if (configuracaoBoleto == null) {

			throw new SysDescException(MensagemConstants.MENSAGEM_CONFIGURACAO_BOLETO_INEXISTENTE);
		}

		return configuracaoBoleto;
	}

}
