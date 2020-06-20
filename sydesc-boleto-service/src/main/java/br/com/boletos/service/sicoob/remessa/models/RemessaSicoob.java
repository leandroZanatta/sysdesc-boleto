package br.com.boletos.service.sicoob.remessa.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jrimum.texgit.FlatFile;
import org.jrimum.texgit.Record;

import br.com.sysdesc.boleto.repository.model.Boleto;
import br.com.sysdesc.boleto.repository.model.ConfiguracaoBoleto;
import br.com.sysdesc.boletos.util.RecordUtil;
import br.com.sysdesc.boletos.util.model.sicoob.AgenciaSicoob;
import br.com.sysdesc.boletos.util.model.sicoob.CedenteSicoob;
import br.com.sysdesc.boletos.util.model.sicoob.HeaderArquivoSicoob;
import br.com.sysdesc.boletos.util.model.sicoob.TraillerArquivoSicoob;
import br.com.sysdesc.util.classes.ContadorUtil;
import br.com.sysdesc.util.classes.ListUtil;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.enumeradores.TipoClienteEnum;
import br.com.sysdesc.util.exception.SysDescException;

public class RemessaSicoob {
	private ContadorUtil contadorUtilLote = new ContadorUtil();
	private AgenciaSicoob agencia = new AgenciaSicoob();
	private CedenteSicoob cedente = new CedenteSicoob();

	private final HeaderArquivoSicoob headerArquivoSicoob = new HeaderArquivoSicoob();
	private final ConfiguracaoBoleto configuracaoBoleto;
	private final List<LoteSicoob> lotes = new ArrayList<>();

	public RemessaSicoob(Long codigoRemessaArquivo, ConfiguracaoBoleto configuracaoBoleto) {
		this.configuracaoBoleto = configuracaoBoleto;

		this.criarAgencia();
		this.criarCedente();

		headerArquivoSicoob.setDataGeracao(new SimpleDateFormat("ddMMyyyy").format(new Date()));
		headerArquivoSicoob.setHoraGeracao(new SimpleDateFormat("HHmmss").format(new Date()));
		headerArquivoSicoob.setCodigoRemessa(codigoRemessaArquivo);
	}

	public void criarLote(Long numeroRemessaLote) {

		lotes.add(new LoteSicoob(contadorUtilLote, numeroRemessaLote, agencia, cedente));
	}

	public void criarDetalhe(Boleto boleto) {

		if (ListUtil.isNullOrEmpty(this.lotes)) {
			throw new SysDescException(MensagemConstants.MENSAGEM_GERAR_LOTE);
		}

		lotes.get(lotes.size() - 1).addDetalhe(boleto);
	}

	private void criarCedente() {

		TipoClienteEnum tipoClienteEnum = TipoClienteEnum.findByCodigo(configuracaoBoleto.getFlagTipoCliente());

		cedente.setTipoCliente(TipoClienteEnum.PESSOA_FISICA.equals(tipoClienteEnum) ? "1" : "2");
		cedente.setCgc(Long.valueOf(StringUtil.formatarNumero(configuracaoBoleto.getCgc())));
		cedente.setNome(configuracaoBoleto.getNome());

		this.headerArquivoSicoob.setCedente(cedente);
	}

	private void criarAgencia() {

		String codigoAgencia = StringUtil.formatarNumero(configuracaoBoleto.getNumeroAgencia());
		String codigoConta = StringUtil.formatarNumero(configuracaoBoleto.getBanco().getNumeroConta());

		String numeroAgencia = codigoAgencia.substring(0, codigoAgencia.length() - 1);
		String numeroConta = codigoConta.substring(0, codigoConta.length() - 1);

		agencia.setAgencia(Long.valueOf(numeroAgencia));
		agencia.setConta(Long.valueOf(numeroConta));
		agencia.setDvAgencia(codigoAgencia.substring(codigoAgencia.length() - 1, codigoAgencia.length()));
		agencia.setDvConta(codigoConta.substring(codigoConta.length() - 1, codigoConta.length()));

		this.headerArquivoSicoob.setAgencia(agencia);
	}

	public void build(FlatFile<Record> records) {

		if (ListUtil.isNullOrEmpty(this.lotes)) {
			throw new SysDescException(MensagemConstants.MENSAGEM_GERAR_LOTE);
		}

		RecordUtil.createRecord(headerArquivoSicoob, records);

		lotes.forEach(lote -> lote.build(records));

		RecordUtil.createRecord(createTraillerArquivo(), records);

	}

	private TraillerArquivoSicoob createTraillerArquivo() {

		Long numeroLotes = Long.valueOf(lotes.size());

		TraillerArquivoSicoob traillerArquivoSicoob = new TraillerArquivoSicoob();
		traillerArquivoSicoob.setQuantidadeLotes(numeroLotes);
		traillerArquivoSicoob.setQuantidadeRegistros(numeroLotes);

		return traillerArquivoSicoob;
	}

}
