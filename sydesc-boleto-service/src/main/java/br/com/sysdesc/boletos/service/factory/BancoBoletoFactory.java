package br.com.sysdesc.boletos.service.factory;

import br.com.sysdesc.boletos.repository.dao.ConfiguracaoBoletoDAO;
import br.com.sysdesc.boletos.repository.model.Banco;
import br.com.sysdesc.boletos.repository.model.ConfiguracaoBoleto;
import br.com.sysdesc.boletos.service.BancoBoletoService;
import br.com.sysdesc.boletos.service.boleto.sicoob.BoletoSicoobService;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.enumeradores.TipoBancoEnum;
import br.com.sysdesc.util.exception.SysDescException;

public class BancoBoletoFactory {

	private final ConfiguracaoBoletoDAO configuracaoBoletoDAO = new ConfiguracaoBoletoDAO();

	public BancoBoletoService getBancoBoleto(Banco banco) {

		ConfiguracaoBoleto configuracaoBoleto = buscarConfiguracaoBoleto(banco.getIdBanco());

		switch (TipoBancoEnum.forCodigo(banco.getNumeroBanco())) {
		case BANCO_SICOOB:
			return new BoletoSicoobService(configuracaoBoleto);

		default:
			throw new SysDescException(MensagemConstants.BANCO_NAO_SUPORTADO_BOLETO);
		}

	}

	public void atualizarNossoNumero(ConfiguracaoBoleto configuracaoBoleto) {

		Long nossoNumeroAtual = configuracaoBoleto.getNossoNumero();

		configuracaoBoleto.setNossoNumero(++nossoNumeroAtual);

		configuracaoBoletoDAO.salvar(configuracaoBoleto);
	}

	public ConfiguracaoBoleto buscarConfiguracaoBoleto(Long codigoBanco) {

		ConfiguracaoBoleto configuracaoBoleto = configuracaoBoletoDAO.buscarConfiguracao(codigoBanco);

		if (configuracaoBoleto == null) {
			throw new SysDescException(MensagemConstants.MENSAGEM_CONFIGURACAO_BOLETO_INEXISTENTE);
		}

		return configuracaoBoleto;
	}

}
