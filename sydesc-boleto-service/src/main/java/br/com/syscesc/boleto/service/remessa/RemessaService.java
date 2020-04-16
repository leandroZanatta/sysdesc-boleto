package br.com.syscesc.boleto.service.remessa;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jrimum.texgit.FlatFile;
import org.jrimum.texgit.Record;

import br.com.boletos.service.factory.RemessaFactory;
import br.com.sysdesc.boleto.repository.dao.BancoDAO;
import br.com.sysdesc.boleto.repository.dao.BoletoDAO;
import br.com.sysdesc.boleto.repository.model.Banco;
import br.com.sysdesc.boleto.repository.model.Boleto;
import br.com.sysdesc.util.classes.ListUtil;
import br.com.sysdesc.util.enumeradores.TipoStatusBoletoEnum;
import br.com.sysdesc.util.resources.Configuracoes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemessaService {

	private RemessaFactory remessaFactory = new RemessaFactory();

	private BoletoDAO boletoDAO = new BoletoDAO();

	private BancoDAO bancoDAO = new BancoDAO();

	public RemessaService() {

	}

	public void processarRemessaBancosSuportados() {

		bancoDAO.buscarBancosSuporteBoleto().forEach(this::gerarRemessa);
	}

	private void gerarRemessa(Banco banco) {

		List<Boleto> boletosParaEnviar = boletoDAO.buscarBoletosParaEnvio(banco.getNumeroBanco());

		if (!ListUtil.isNullOrEmpty(boletosParaEnviar)) {
			try {

				FlatFile<Record> records = remessaFactory.getRemessaBuilder(banco).build(boletosParaEnviar);

				FileUtils.writeLines(new File(Configuracoes.FOLDER_TRANSFER + "/Remessa.rem"), records.write(), "\r\n");

				boletosParaEnviar
						.forEach(boleto -> boleto.setCodigoStatus(TipoStatusBoletoEnum.ENVIANDO_REMESSA.getCodigo()));

				boletoDAO.salvar(boletosParaEnviar);

			} catch (Exception e) {

				log.error("Erro Gerando Remessa", e);
			}
		}
	}

}
