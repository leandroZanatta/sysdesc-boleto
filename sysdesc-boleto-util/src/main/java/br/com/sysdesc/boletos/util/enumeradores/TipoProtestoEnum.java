package br.com.sysdesc.boletos.util.enumeradores;

import java.util.HashMap;
import java.util.Map;

public enum TipoProtestoEnum {

    DIAS_CORRIDOS(1L, "Protestar Dias Corridos"),

    NAO_PROTESTAR(3L, "Não Protestar/Não Negativar"),

    NEGATIVACAO_SEM_PROTESTO(8L, "Negativação sem protesto");

    private static Map<Long, TipoProtestoEnum> mapa = new HashMap<>();

    static {

        for (TipoProtestoEnum programa : TipoProtestoEnum.values()) {
            mapa.put(programa.getCodigo(), programa);
        }
    }

    private final Long codigo;

    private final String descricao;

    TipoProtestoEnum(Long codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Long getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoProtestoEnum findByCodigo(Long codigoProtesto) {
        return mapa.get(codigoProtesto);
    }

    @Override
    public String toString() {

        return descricao;
    }
}
