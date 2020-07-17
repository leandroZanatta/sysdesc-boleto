package br.com.sysdesc.boletos.service.factory;

import br.com.sysdesc.boletos.repository.dao.ConfiguracaoBoletoDAO;
import br.com.sysdesc.boletos.repository.model.Banco;
import br.com.sysdesc.boletos.repository.model.ConfiguracaoBoleto;
import br.com.sysdesc.boletos.service.builders.RemessaBuilder;
import br.com.sysdesc.boletos.service.sicoob.remessa.RemessaSicoobBuilder;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.enumeradores.TipoBancoEnum;
import br.com.sysdesc.util.exception.SysDescException;

public class RemessaFactory {

	private final ConfiguracaoBoletoDAO configuracaoBoletoDAO = new ConfiguracaoBoletoDAO();

	public RemessaBuilder getRemessaBuilder(Banco banco) {

		ConfiguracaoBoleto configuracaoBoleto = buscarConfiguracaoBoleto(banco.getIdBanco());

		switch (TipoBancoEnum.forCodigo(banco.getNumeroBanco())) {
		case BANCO_SICOOB:
			return new RemessaSicoobBuilder(configuracaoBoleto);

		default:
			throw new SysDescException(MensagemConstants.BANCO_NAO_SUPORTADO_BOLETO);
		}
	}

	public ConfiguracaoBoleto buscarConfiguracaoBoleto(Long codigoBanco) {

		ConfiguracaoBoleto configuracaoBoleto = configuracaoBoletoDAO.buscarConfiguracao(codigoBanco);

		if (configuracaoBoleto == null) {

			throw new SysDescException(MensagemConstants.MENSAGEM_CONFIGURACAO_BOLETO_INEXISTENTE);
		}

		return configuracaoBoleto;
	}

}
