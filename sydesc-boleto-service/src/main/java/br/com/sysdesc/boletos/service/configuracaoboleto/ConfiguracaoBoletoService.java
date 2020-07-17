package br.com.sysdesc.boletos.service.configuracaoboleto;

import br.com.sysdesc.boletos.repository.dao.ConfiguracaoBoletoDAO;
import br.com.sysdesc.boletos.repository.model.ConfiguracaoBoleto;
import br.com.sysdesc.boletos.util.enumeradores.TipoMultaJurosEnum;
import br.com.sysdesc.boletos.util.enumeradores.TipoProtestoEnum;
import br.com.sysdesc.pesquisa.service.impl.AbstractPesquisableServiceImpl;
import br.com.sysdesc.util.classes.BigDecimalUtil;
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

		if (StringUtil.isNullOrEmpty(objetoPersistir.getModalidade())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_MODALIDADE);
		}

		if (LongUtil.isNullOrZero(objetoPersistir.getDiasMaximoPagamento())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_MAXIMO_DIAS_PAGAMENTO);
		}

		if (LongUtil.isNullOrZero(objetoPersistir.getCodigoProtesto())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_SELECIONE_FORMA_PROTESTO);
		}

		if (LongUtil.isNullOrZero(objetoPersistir.getCodigoMulta())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_SELECIONE_FORMA_MULTA);
		}

		if (LongUtil.isNullOrZero(objetoPersistir.getCodigoJurosMora())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_SELECIONE_FORMA_JUROS);
		}

		if (TipoProtestoEnum.findByCodigo(objetoPersistir.getCodigoProtesto()).equals(TipoProtestoEnum.DIAS_CORRIDOS)
				&& LongUtil.isNullOrZero(objetoPersistir.getDiasProtesto())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_DIAS_PROTESTO_MAIOR_ZERO);
		}

		if (!TipoMultaJurosEnum.findByCodigo(objetoPersistir.getCodigoJurosMora()).equals(TipoMultaJurosEnum.ISENTO)
				&& LongUtil.isNullOrZero(objetoPersistir.getDiasJurosMora())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_DIAS_JURO_MAIOR_ZERO);
		}

		if (!TipoMultaJurosEnum.findByCodigo(objetoPersistir.getCodigoJurosMora()).equals(TipoMultaJurosEnum.ISENTO)
				&& BigDecimalUtil.isNullOrZero(objetoPersistir.getValorJurosMora())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_VALOR_JURO_MAIOR_ZERO);
		}

		if (!TipoMultaJurosEnum.findByCodigo(objetoPersistir.getCodigoMulta()).equals(TipoMultaJurosEnum.ISENTO)
				&& LongUtil.isNullOrZero(objetoPersistir.getDiasMulta())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_DIAS_MULTA_MAIOR_ZERO);
		}

		if (!TipoMultaJurosEnum.findByCodigo(objetoPersistir.getCodigoMulta()).equals(TipoMultaJurosEnum.ISENTO)
				&& BigDecimalUtil.isNullOrZero(objetoPersistir.getValorMulta())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_VALOR_MULTA_MAIOR_ZERO);
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
