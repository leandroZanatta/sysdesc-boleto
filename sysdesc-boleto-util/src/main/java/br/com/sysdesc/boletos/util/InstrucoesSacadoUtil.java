package br.com.sysdesc.boletos.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Date;

import br.com.sysdesc.boletos.util.enumeradores.TipoMultaJurosEnum;
import br.com.sysdesc.boletos.util.enumeradores.TipoProtestoEnum;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.classes.StringUtil;

public class InstrucoesSacadoUtil {

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    static {
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
    }

    public static String montarProtesto(Long codigoProtesto, Date dataLimiteProtesto) {

        if (TipoProtestoEnum.DIAS_CORRIDOS.getCodigo().equals(codigoProtesto)) {

            return String.format("NÃO RECEBER APÓS %S", DateUtil.format(DateUtil.FORMATO_DD_MM_YYY, dataLimiteProtesto));
        }

        return StringUtil.STRING_VAZIA;
    }

    public static String montarMulta(Long codigoMulta, BigDecimal valorBoleto, BigDecimal valorMulta) {

        if (TipoMultaJurosEnum.TAXA_MENSAL.getCodigo().equals(codigoMulta)) {

            return String.format("APÓS VENCIMENTO COBRAR MULTA DE %s",
                    numberFormat.format(calcularValorPorPercentual(valorBoleto, valorMulta).doubleValue()));
        }

        if (TipoMultaJurosEnum.VALOR_DIA.getCodigo().equals(codigoMulta)) {

            return String.format("APÓS VENC. COBRAR MULTA DE R$%s AO DIA",
                    numberFormat.format(calcularValorPorPercentual(valorBoleto, valorMulta).doubleValue()));
        }

        return StringUtil.STRING_VAZIA;
    }

    public static String montarJurosMora(Long codigoJuros, BigDecimal valorBoleto, BigDecimal valorJuros) {

        if (TipoMultaJurosEnum.TAXA_MENSAL.getCodigo().equals(codigoJuros)) {

            return String.format("APÓS VENC. COBRAR JUROS DE %s%% AO MÊS", numberFormat.format(valorJuros));
        }

        if (TipoMultaJurosEnum.VALOR_DIA.getCodigo().equals(codigoJuros)) {

            return String.format("APÓS VENC. COBRAR JUROS DE R$ %s AO DIA",
                    numberFormat.format(calcularValorPorPercentual(valorBoleto, valorJuros).doubleValue()));
        }

        return StringUtil.STRING_VAZIA;
    }

    public static BigDecimal calcularValorPorPercentual(BigDecimal valorTotal, BigDecimal percentual) {

        return valorTotal.multiply(percentual.divide(BigDecimal.valueOf(100.0), 4, RoundingMode.HALF_EVEN));
    }
}
