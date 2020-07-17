package br.com.sysdesc.boletos.service.retorno.models;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class RetornoDetalheModel {

	private Date dataCredito;

	private BigDecimal acrescimos;

	private BigDecimal descontos;

	private BigDecimal valorPago;

}
