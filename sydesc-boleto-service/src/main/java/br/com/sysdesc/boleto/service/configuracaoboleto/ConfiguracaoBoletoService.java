package br.com.sysdesc.boleto.service.configuracaoboleto;

import br.com.sysdesc.boleto.repository.dao.ConfiguracaoBoletoDAO;
import br.com.sysdesc.boleto.repository.model.ConfiguracaoBoleto;
import br.com.sysdesc.pesquisa.service.impl.AbstractPesquisableServiceImpl;
import br.com.sysdesc.util.classes.CPFUtil;
import br.com.sysdesc.util.classes.LongUtil;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.enumeradores.TipoClienteEnum;
import br.com.sysdesc.util.exception.SysDescException;

public class ConfiguracaoBoletoService extends AbstractPesquisableServiceImpl<ConfiguracaoBoleto> {

	private ConfiguracaoBoletoDAO configuracaoBoletoDAO;

	public ConfiguracaoBoletoService() {
		this(new ConfiguracaoBoletoDAO());
	}

	public ConfiguracaoBoletoService(ConfiguracaoBoletoDAO configuracaoBoletoDAO) {
		super(configuracaoBoletoDAO, ConfiguracaoBoleto::getIdConfiguracaoBoleto);

		this.configuracaoBoletoDAO = configuracaoBoletoDAO;
	}

	@Override
	public void validar(ConfiguracaoBoleto objetoPersistir) {

		if (objetoPersistir.getBanco() == null) {
			throw new SysDescException(MensagemConstants.MENSAGEM_BANCO_NAO_INFORMADO);
		}

		if (StringUtil.isNullOrEmpty(objetoPersistir.getFlagTipoCliente())) {
			throw new SysDescException(MensagemConstants.MENSAGEM_SELECIONE_TIPO_CLIENTE);
		}

		boolean pessoaFisica = objetoPersistir.getFlagTipoCliente().equals(TipoClienteEnum.PESSOA_FISICA.getCodigo());

		if (pessoaFisica) {

			if (StringUtil.isNullOrEmpty(StringUtil.formatarNumero(objetoPersistir.getCgc()))) {
				throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_CPF);
			}

			if (!CPFUtil.isCPFValido(objetoPersistir.getCgc())) {
				throw new SysDescException(MensagemConstants.MENSAGEM_CPF_INVALIDO);
			}

		} else {

			if (StringUtil.isNullOrEmpty(StringUtil.formatarNumero(objetoPersistir.getCgc()))) {
				throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_CNPJ);
			}

			if (CPFUtil.isCPFValido(objetoPersistir.getCgc())) {
				throw new SysDescException(MensagemConstants.MENSAGEM_CNPJ_INVALIDO);
			}

		}

		if (StringUtil.isNullOrEmpty(objetoPersistir.getNome())) {
			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_NOME);
		}

		if (LongUtil.isNullOrZero(objetoPersistir.getCodigoCarteira())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_CARTEIRA);
		}

		if (StringUtil.isNullOrEmpty(StringUtil.formatarNumero(objetoPersistir.getNumeroConta()))) {
			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_CONTA_VALIDA);
		}

		if (StringUtil.isNullOrEmpty(StringUtil.formatarNumero(objetoPersistir.getNumeroAgencia()))) {
			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_AGENCIA_VALIDA);
		}

		if (LongUtil.isNullOrZero(objetoPersistir.getNossoNumero())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_NOSSO_NUMERO);
		}
	}

	public ConfiguracaoBoleto buscarConfiguracao(Long codigoBanco) {

		return configuracaoBoletoDAO.buscarConfiguracao(codigoBanco);
	}

}
