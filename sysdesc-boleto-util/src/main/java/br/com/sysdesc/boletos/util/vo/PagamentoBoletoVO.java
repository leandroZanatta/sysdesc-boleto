package br.com.sysdesc.boletos.util.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagamentoBoletoVO {

    private Long numeroDocumento;

    private final BigDecimal valorPagamento;

    private final Date dataVencimento;

}