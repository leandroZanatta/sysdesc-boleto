package br.com.sysdesc.boletos.service.sicoob.retorno;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.event.EventListenerList;

import br.com.sysdesc.arquivos.core.FileManager;
import br.com.sysdesc.arquivos.exceptions.SysdescArquivosException;
import br.com.sysdesc.boletos.repository.dao.BancoDAO;
import br.com.sysdesc.boletos.repository.dao.BoletoDAO;
import br.com.sysdesc.boletos.repository.dao.RetornoDAO;
import br.com.sysdesc.boletos.repository.model.Banco;
import br.com.sysdesc.boletos.repository.model.Boleto;
import br.com.sysdesc.boletos.repository.model.Retorno;
import br.com.sysdesc.boletos.repository.model.RetornoBoleto;
import br.com.sysdesc.boletos.service.builders.RetornoBuilder;
import br.com.sysdesc.boletos.service.enumeradores.TipoRetornoMovimentoTEnum;
import br.com.sysdesc.boletos.service.retorno.listener.RetonoRemessaBoletoAutorizadoActionListener;
import br.com.sysdesc.boletos.service.retorno.listener.RetonoRemessaBoletoPagoActionListener;
import br.com.sysdesc.boletos.service.retorno.listener.RetornoActionListener;
import br.com.sysdesc.boletos.service.retorno.models.RetornoDetalheModel;
import br.com.sysdesc.boletos.service.sicoob.retorno.models.RetornoSicoobDetalheModel;
import br.com.sysdesc.boletos.service.sicoob.retorno.models.RetornoSicoobHeaderArquivoModel;
import br.com.sysdesc.boletos.service.sicoob.retorno.models.RetornoSicoobModel;
import br.com.sysdesc.util.classes.ClassTypeUtil;
import br.com.sysdesc.util.classes.LongUtil;
import br.com.sysdesc.util.enumeradores.TipoBancoEnum;
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

	protected EventListenerList retornoListener = new EventListenerList();

	public RetornoSicoobBuilder(List<String> records) {
		this.records = records;
	}

	@Override
	public void build() throws SysdescArquivosException {

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

		retornoDAO.salvar(retorno);
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

		return Arrays.asList(TipoRetornoMovimentoTEnum.values()).stream()
				.anyMatch(enumerador -> processarEntrada(enumerador, mapaDetalhes, banco, retorno));
	}

	private boolean processarEntrada(TipoRetornoMovimentoTEnum tipoMovimento, Map<Long, List<RetornoSicoobDetalheModel>> mapaDetalhes, Banco banco,
			Retorno retorno) {

		if (mapaDetalhes.containsKey(tipoMovimento.getCodigo())) {

			return mapaDetalhes.get(tipoMovimento.getCodigo()).stream()
					.anyMatch(boleto -> procesarEntradaBoleto(tipoMovimento, boleto, banco, retorno));
		}

		return false;
	}

	private boolean procesarEntradaBoleto(TipoRetornoMovimentoTEnum tipoMovimento, RetornoSicoobDetalheModel boletoDetalhe, Banco banco,
			Retorno retorno) {

		Boleto boleto = retornarBoletoNossoNumero(boletoDetalhe, banco);

		RetornoBoleto retornoBoleto = new RetornoBoleto();
		retornoBoleto.setDataCadastro(new Date());
		retornoBoleto.setTipoRetorno(TipoRetornoMovimentoTEnum.ENTRADA_CONFIRMADA.getCodigo());

		if (boleto != null) {

			try {

				fireListener(tipoMovimento.getListener(), boleto, convertDetalhe(boletoDetalhe), banco);

			} catch (Exception e) {

				retornoBoleto.setCodigoStatus(TipoStatusRetornoBoletoEnum.ERRO.getCodigo());

				retorno.addRetornoBoletos(retornoBoleto);

				return false;
			}

			boleto.setCodigoStatus(tipoMovimento.getTipoStatusBoletoEnum().getCodigo());

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

	private RetornoDetalheModel convertDetalhe(RetornoSicoobDetalheModel boletoDetalhe) {

		RetornoDetalheModel retornoDetalheModel = new RetornoDetalheModel();

		retornoDetalheModel.setAcrescimos(boletoDetalhe.getRetornoSicoobDetalheSegmentoUModel().getAcrescimos());
		retornoDetalheModel.setDescontos(boletoDetalhe.getRetornoSicoobDetalheSegmentoUModel().getDescontos());
		retornoDetalheModel.setValorPago(boletoDetalhe.getRetornoSicoobDetalheSegmentoUModel().getValorPago());
		retornoDetalheModel.setDataCredito(parseDate(boletoDetalhe.getRetornoSicoobDetalheSegmentoUModel().getDataCredito()));

		return retornoDetalheModel;
	}

	private Date parseDate(String data) {

		try {
			return new SimpleDateFormat("ddMMyyyy").parse(data);

		} catch (ParseException e) {
			return null;
		}
	}

	private void fireListener(Class<? extends RetornoActionListener> listener, Boleto boleto, RetornoDetalheModel boletoDetalhe, Banco banco)
			throws IllegalAccessException, InvocationTargetException {

		Object[] listeners = retornoListener.getListenerList();

		for (int i = 0; i < listeners.length; i = i + 2) {

			if (listeners[i] == listener) {

				findAndCallCorrectListener((RetornoActionListener) listeners[i + 1], boleto, boletoDetalhe);
			}
		}
	}

	private void findAndCallCorrectListener(RetornoActionListener listener, Boleto boleto,
			RetornoDetalheModel boletoDetalhe) throws IllegalAccessException, InvocationTargetException {

		Optional<Method> optionalMethod = Arrays.asList(listener.getClass().getDeclaredMethods()).stream().findFirst();

		if (optionalMethod.isPresent()) {

			optionalMethod.get().invoke(listener, ClassTypeUtil.getCorrectMethodParameters(optionalMethod.get(), boleto, boletoDetalhe));
		}
	}

	private Boleto retornarBoletoNossoNumero(RetornoSicoobDetalheModel boletoDetalhe, Banco banco) {
		String nossoNumero = boletoDetalhe.getRetornoSicoobDetalheSegmentoTModel().getNossoNumero().substring(0, 10);

		return boletoDAO.buscarBoletosPorNossoNumero(banco.getNumeroBanco(), banco.getNumeroAgencia(), banco.getNumeroConta(), nossoNumero);
	}

	public Long mapByCodigoMovimento(RetornoSicoobDetalheModel retornoDetalhe) {

		return retornoDetalhe.getRetornoSicoobDetalheSegmentoTModel().getCodigoMovimento();
	}

	@Override
	public void addRetornoRemessaAutorizadoListener(RetonoRemessaBoletoAutorizadoActionListener retornoActionListener) {

		retornoListener.add(RetonoRemessaBoletoAutorizadoActionListener.class, retornoActionListener);
	}

	@Override
	public void addRetornoRemessaPagoListener(RetonoRemessaBoletoPagoActionListener retornoActionListener) {

		retornoListener.add(RetonoRemessaBoletoPagoActionListener.class, retornoActionListener);
	}

}
