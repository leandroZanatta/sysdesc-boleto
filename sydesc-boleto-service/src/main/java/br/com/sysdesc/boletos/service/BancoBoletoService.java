package br.com.sysdesc.boletos.service;

import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;

public interface BancoBoletoService {

	public ContaBancaria createContaBancaria();

	public Cedente gerarCedente();

	public String gerarNossoNumero();

}
