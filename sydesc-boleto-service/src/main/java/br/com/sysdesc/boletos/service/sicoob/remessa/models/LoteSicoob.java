package br.com.sysdesc.boletos.service.sicoob.remessa.models;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jrimum.texgit.FlatFile;
import org.jrimum.texgit.Record;

import br.com.sysdesc.boletos.repository.model.Boleto;
import br.com.sysdesc.boletos.util.RecordUtil;
import br.com.sysdesc.boletos.util.model.sicoob.AgenciaSicoob;
import br.com.sysdesc.boletos.util.model.sicoob.CedenteSicoob;
import br.com.sysdesc.boletos.util.model.sicoob.HeaderLoteSicoob;
import br.com.sysdesc.boletos.util.model.sicoob.TraillerLoteSicoob;
import br.com.sysdesc.util.classes.ContadorUtil;

public class LoteSicoob {

	private ContadorUtil contadorUtilDetalhes = new ContadorUtil();

	private final AgenciaSicoob agencia;

	private final List<DetalheSicoob> detalheSicoobs = new ArrayList<>();

	private final HeaderLoteSicoob headerLoteSicoob = new HeaderLoteSicoob();
	private final TraillerLoteSicoob traillerLoteSicoob = new TraillerLoteSicoob();

	public LoteSicoob(ContadorUtil contadorLotes, Long numeroRemessaLote, AgenciaSicoob agencia, CedenteSicoob cedente) {
		this.agencia = agencia;

		headerLoteSicoob.setAgencia(agencia);
		headerLoteSicoob.setCedente(cedente);
		headerLoteSicoob.setNumeroLote(contadorLotes.next());
		headerLoteSicoob.setNumeroRemessa(numeroRemessaLote);
		headerLoteSicoob.setDataGeracao(new SimpleDateFormat("ddMMyyyy").format(new Date()));

		traillerLoteSicoob.setNumeroLote(contadorLotes.getValue());
	}

	public void addDetalhe(Boleto boleto) {

		detalheSicoobs.add(new DetalheSicoob(headerLoteSicoob.getNumeroLote(), contadorUtilDetalhes, agencia, boleto));
	}

	public List<DetalheSicoob> getDetalheSicoobs() {
		return detalheSicoobs;
	}

	public HeaderLoteSicoob getHeaderLoteSicoob() {
		return headerLoteSicoob;
	}

	public void build(FlatFile<Record> records) {

		RecordUtil.createRecord(headerLoteSicoob, records);

		detalheSicoobs.forEach(detalhe -> detalhe.build(records));

		totalizarTrailler();

		RecordUtil.createRecord(traillerLoteSicoob, records);
	}

	private void totalizarTrailler() {

		Long quantidade = Long.valueOf(detalheSicoobs.size());

		BigDecimal totalTitulos = detalheSicoobs.stream().map(detalhe -> detalhe.getDetalhePSicoob().getValorNominal()).reduce(BigDecimal.ZERO,
				(detalhe, acululador) -> detalhe.add(acululador));

		traillerLoteSicoob.setQuantidadeRegistros(quantidade);
		traillerLoteSicoob.setQuantidadeTitulosCobrancaSimples(quantidade);
		traillerLoteSicoob.setTotalizacaoCobrancaSimples(totalTitulos);

		// Perguntar o que é isto.
		traillerLoteSicoob.setQuantidadeTitulosCobrancaCaucionada(0L);
		traillerLoteSicoob.setQuantidadeTitulosCobrancaDescontada(0L);
		traillerLoteSicoob.setQuantidadeTitulosCobrancaVinculada(0L);
		traillerLoteSicoob.setTotalizacaoCobrancaCaucionada(BigDecimal.ZERO);
		traillerLoteSicoob.setTotalizacaoCobrancaDescontada(BigDecimal.ZERO);
		traillerLoteSicoob.setTotalizacaoCobrancaVinculada(BigDecimal.ZERO);

	}
}
