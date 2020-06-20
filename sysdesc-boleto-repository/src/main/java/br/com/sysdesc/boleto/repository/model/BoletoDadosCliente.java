package br.com.sysdesc.boleto.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_boletodadoscliente")
public class BoletoDadosCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cd_boleto", referencedColumnName = "id_boleto")
	private Boleto boleto;

	@Column(name = "tx_cgc")
	private String cgc;

	@Column(name = "tx_nome")
	private String nome;

	@Column(name = "tx_uf")
	private String uf;

	@Column(name = "fl_tipocliente")
	private String flagTipoCliente;

	@Column(name = "tx_endereco")
	private String endereco;

	@Column(name = "tx_numero")
	private String numero;

	@Column(name = "tx_bairro")
	private String bairro;

	@Column(name = "tx_cep")
	private String cep;

	@Column(name = "tx_cidade")
	private String cidade;

}