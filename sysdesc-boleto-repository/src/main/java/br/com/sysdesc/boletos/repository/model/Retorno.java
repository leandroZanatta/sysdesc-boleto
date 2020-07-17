package br.com.sysdesc.boletos.repository.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_retorno")
@SequenceGenerator(name = "GEN_RETORNO", allocationSize = 1, sequenceName = "GEN_RETORNO")
public class Retorno implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_RETORNO")
	@Column(name = "id_retorno")
	private Long idRetorno;

	@Column(name = "nr_banco")
	private Long numeroBanco;

	@Column(name = "nr_retorno")
	private Long numeroRetono;

	@Column(name = "bl_arquivo")
	private byte[] arquivo;

	@Column(name = "dt_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;

	@Column(name = "cd_status")
	private Long codigoStatus;

	@OneToMany(mappedBy = "retorno", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RetornoBoleto> retornoBoletos = new ArrayList<>();

	public void addRetornoBoletos(RetornoBoleto retornoBoleto) {

		retornoBoleto.setRetorno(this);

		this.getRetornoBoletos().add(retornoBoleto);
	}

}