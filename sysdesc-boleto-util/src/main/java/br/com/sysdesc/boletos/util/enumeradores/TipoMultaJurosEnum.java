package br.com.sysdesc.boletos.util.enumeradores;

import java.util.HashMap;
import java.util.Map;

public enum TipoMultaJurosEnum {

    ISENTO(0L, "Isento"),

    VALOR_DIA(1L, "Valor por dia"),

    TAXA_MENSAL(2L, "Taxa mensal");

    private static Map<Long, TipoMultaJurosEnum> mapa = new HashMap<>();

    static {

        for (TipoMultaJurosEnum programa : TipoMultaJurosEnum.values()) {
            mapa.put(programa.getCodigo(), programa);
        }
    }

    private final Long codigo;

    private final String descricao;

    TipoMultaJurosEnum(Long codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Long getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoMultaJurosEnum findByCodigo(Long codigo) {
        return mapa.get(codigo);
    }

    @Override
    public String toString() {

        return descricao;
    }
}
