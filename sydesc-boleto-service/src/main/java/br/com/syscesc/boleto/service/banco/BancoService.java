package br.com.syscesc.boleto.service.banco;

import br.com.sysdesc.boleto.repository.dao.BancoDAO;
import br.com.sysdesc.boleto.repository.model.Banco;
import br.com.sysdesc.pesquisa.service.impl.AbstractPesquisableServiceImpl;
import br.com.sysdesc.util.classes.LongUtil;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.exception.SysDescException;

public class BancoService extends AbstractPesquisableServiceImpl<Banco> {

	public BancoService() {
		super(new BancoDAO(), Banco::getIdBanco);
	}

	@Override
	public void validar(Banco objetoPersistir) {

		if (LongUtil.isNullOrZero(objetoPersistir.getNumeroBanco())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_SELECIONE_BANCO);
		}

		if (StringUtil.isNullOrEmpty(objetoPersistir.getNumeroAgencia())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_AGENCIA_VALIDA);
		}

		if (StringUtil.isNullOrEmpty(objetoPersistir.getNumeroConta())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_CONTA_VALIDA);
		}

		if (LongUtil.isNullOrZero(objetoPersistir.getSituacao())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_SELECIONE_SITUACAO);
		}

	}

}
