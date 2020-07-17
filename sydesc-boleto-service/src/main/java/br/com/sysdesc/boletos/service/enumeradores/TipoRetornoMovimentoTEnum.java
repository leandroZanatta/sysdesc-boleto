package br.com.sysdesc.boletos.service.enumeradores;

import static br.com.sysdesc.util.enumeradores.TipoStatusBoletoEnum.AUTORIZADO;
import static br.com.sysdesc.util.enumeradores.TipoStatusBoletoEnum.PAGO;

import java.util.HashMap;
import java.util.Map;

import br.com.sysdesc.boletos.service.retorno.listener.RetonoRemessaBoletoAutorizadoActionListener;
import br.com.sysdesc.boletos.service.retorno.listener.RetonoRemessaBoletoPagoActionListener;
import br.com.sysdesc.boletos.service.retorno.listener.RetornoActionListener;
import br.com.sysdesc.util.enumeradores.TipoStatusBoletoEnum;

public enum TipoRetornoMovimentoTEnum {

	ENTRADA_CONFIRMADA(2L, "Entrada Confirmada", AUTORIZADO, RetonoRemessaBoletoAutorizadoActionListener.class),

	BAIXA(9L, "Baixa", PAGO, RetonoRemessaBoletoPagoActionListener.class);

	private static Map<Long, TipoRetornoMovimentoTEnum> mapa = new HashMap<>();

	static {

		for (TipoRetornoMovimentoTEnum programa : TipoRetornoMovimentoTEnum.values()) {
			mapa.put(programa.getCodigo(), programa);
		}
	}

	private final Long codigo;

	private final String descricao;

	private final Class<? extends RetornoActionListener> listener;

	private final TipoStatusBoletoEnum tipoStatusBoletoEnum;

	TipoRetornoMovimentoTEnum(Long codigo, String descricao, TipoStatusBoletoEnum tipoStatusBoletoEnum,
			Class<? extends RetornoActionListener> listener) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.tipoStatusBoletoEnum = tipoStatusBoletoEnum;
		this.listener = listener;
	}

	public Long getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public TipoStatusBoletoEnum getTipoStatusBoletoEnum() {
		return tipoStatusBoletoEnum;
	}

	public Class<? extends RetornoActionListener> getListener() {
		return listener;
	}

	public static TipoRetornoMovimentoTEnum findByCodigo(Long codigoPrograma) {
		return mapa.get(codigoPrograma);
	}

	@Override
	public String toString() {

		return descricao;
	}
}
