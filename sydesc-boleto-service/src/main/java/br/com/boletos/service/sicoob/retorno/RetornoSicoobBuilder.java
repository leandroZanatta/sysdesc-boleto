package br.com.boletos.service.sicoob.retorno;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.boletos.service.builders.RetornoBuilder;
import br.com.boletos.service.sicoob.retorno.models.RetornoSicoobDetalheModel;
import br.com.boletos.service.sicoob.retorno.models.RetornoSicoobHeaderArquivoModel;
import br.com.boletos.service.sicoob.retorno.models.RetornoSicoobModel;
import br.com.sysdesc.arquivos.core.FileManager;
import br.com.sysdesc.arquivos.exceptions.SysdescArquivosException;
import br.com.sysdesc.boleto.repository.dao.BancoDAO;
import br.com.sysdesc.boleto.repository.dao.BoletoDAO;
import br.com.sysdesc.boleto.repository.dao.RetornoDAO;
import br.com.sysdesc.boleto.repository.model.Banco;
import br.com.sysdesc.boleto.repository.model.Boleto;
import br.com.sysdesc.boleto.repository.model.Retorno;
import br.com.sysdesc.boleto.repository.model.RetornoBoleto;
import br.com.sysdesc.util.classes.LongUtil;
import br.com.sysdesc.util.enumeradores.TipoBancoEnum;
import br.com.sysdesc.util.enumeradores.TipoRetornoMovimentoTEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusBoletoEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusRetornoBoletoEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusRetornoEnum;
import br.com.sysdesc.util.exception.SysDescException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RetornoSicoobBuilder implements RetornoBuilder {

	private List<String> records;

	private RetornoDAO retornoDAO = new RetornoDAO();

	private BancoDAO bancoDAO = new BancoDAO();

	private BoletoDAO boletoDAO = new BoletoDAO();

	public RetornoSicoobBuilder(List<String> records) {
		this.records = records;
	}

	@Override
	public Retorno build() throws SysdescArquivosException {

		RetornoSicoobModel retornoSicoobModel = FileManager.readFile(records, RetornoSicoobModel.class);

		if (retornoDAO.retornoImportado(TipoBancoEnum.BANCO_SICOOB.getCodigo(), retornoSicoobModel.getHeaderArquivo().getSequencial())) {

			throw new SysDescException("Arquivo de retorno já foi importado");
		}

		Retorno retorno = new Retorno();
		retorno.setNumeroBanco(TipoBancoEnum.BANCO_SICOOB.getCodigo());
		retorno.setNumeroRetono(retornoSicoobModel.getHeaderArquivo().getSequencial());
		retorno.setDataCadastro(new Date());
		retorno.setArquivo(this.records.stream().collect(Collectors.joining("\n")).getBytes());
		retorno.setCodigoStatus(TipoStatusRetornoEnum.IMPORTADO.getCodigo());

		Map<Long, List<RetornoSicoobDetalheModel>> mapaDetalhes = retornoSicoobModel.getDetalhe().stream()
				.collect(Collectors.groupingBy(this::mapByCodigoMovimento));

		try {

			Banco banco = getBancoRetorno(retornoSicoobModel.getHeaderArquivo());

			if (processarRetorno(mapaDetalhes, banco, retorno)) {
				retorno.setCodigoStatus(TipoStatusRetornoEnum.PROCESSADO.getCodigo());
			}

		} catch (SysDescException e) {

			retorno.setCodigoStatus(TipoStatusRetornoEnum.ERRO.getCodigo());

			log.error("Erro ao procesar o arquivo de retorno", e);
		}

		return retorno;
	}

	private Banco getBancoRetorno(RetornoSicoobHeaderArquivoModel headerArquivo) {
		Long agencia = concatenarDigitos(headerArquivo.getNumeroAgencia(), headerArquivo.getDvAgencia());
		Long conta = concatenarDigitos(headerArquivo.getNumeroconta(), headerArquivo.getDvConta());

		return bancoDAO.obterBancosComBoleto(TipoBancoEnum.BANCO_SICOOB.getCodigo()).stream()
				.filter(banco -> LongUtil.parseLong(banco.getNumeroAgencia()).equals(agencia)
						&& LongUtil.parseLong(banco.getNumeroConta()).equals(conta))
				.findFirst()
				.orElseThrow(() -> new SysDescException("informações do banco informado no arquivo não foram encontradas"));
	}

	private Long concatenarDigitos(Long principal, Long digito) {

		return Long.valueOf(principal.toString() + digito.toString());
	}

	private boolean processarRetorno(Map<Long, List<RetornoSicoobDetalheModel>> mapaDetalhes, Banco banco, Retorno retorno) {

		boolean processou = false;

		if (mapaDetalhes.containsKey(TipoRetornoMovimentoTEnum.ENTRADA_CONFIRMADA.getCodigo())) {

			processou = procesarEntradaBoletos(mapaDetalhes.get(TipoRetornoMovimentoTEnum.ENTRADA_CONFIRMADA.getCodigo()), banco, retorno);
		}

		return processou;
	}

	private boolean procesarEntradaBoletos(List<RetornoSicoobDetalheModel> entradaBoletos, Banco banco, Retorno retorno) {

		if (!entradaBoletos.isEmpty()) {

			return entradaBoletos.stream().anyMatch(boleto -> processarEntradaBoleto(boleto, banco, retorno));
		}

		return false;
	}

	private boolean processarEntradaBoleto(RetornoSicoobDetalheModel boletoDetalhe, Banco banco, Retorno retorno) {

		String nossoNumero = boletoDetalhe.getRetornoSicoobDetalheSegmentoTModel().getNossoNumero().substring(0, 10);

		Boleto boleto = boletoDAO.buscarBoletosPorNossoNumero(banco.getNumeroBanco(), banco.getNumeroAgencia(), banco.getNumeroConta(), nossoNumero);

		RetornoBoleto retornoBoleto = new RetornoBoleto();
		retornoBoleto.setDataCadastro(new Date());
		retornoBoleto.setTipoRetorno(TipoRetornoMovimentoTEnum.ENTRADA_CONFIRMADA.getCodigo());

		if (boleto != null) {

			boleto.setCodigoStatus(TipoStatusBoletoEnum.AUTORIZADO.getCodigo());

			boletoDAO.salvar(boleto);

			retornoBoleto.setBoleto(boleto);
			retornoBoleto.setCodigoStatus(TipoStatusRetornoBoletoEnum.PROCESSADO.getCodigo());

			retorno.addRetornoBoletos(retornoBoleto);

			return true;
		}

		retornoBoleto.setCodigoStatus(TipoStatusRetornoBoletoEnum.BOLETO_INEXISTENTE.getCodigo());
		retorno.addRetornoBoletos(retornoBoleto);

		return false;
	}

	public Long mapByCodigoMovimento(RetornoSicoobDetalheModel retornoDetalhe) {

		return retornoDetalhe.getRetornoSicoobDetalheSegmentoTModel().getCodigoMovimento();
	}

}
