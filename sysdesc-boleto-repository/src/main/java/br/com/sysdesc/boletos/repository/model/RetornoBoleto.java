package br.com.sysdesc.boletos.repository.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_retornoboleto")
@SequenceGenerator(name = "GEN_RETORNOBOLETO", allocationSize = 1, sequenceName = "GEN_RETORNOBOLETO")
public class RetornoBoleto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_RETORNOBOLETO")
	@Column(name = "id_retornoboleto")
	private Long idRetornoBoleto;

	@ManyToOne
	@JoinColumn(name = "cd_retorno")
	private Retorno retorno;

	@ManyToOne
	@JoinColumn(name = "cd_boleto")
	private Boleto boleto;

	@Column(name = "nr_tiporetorno")
	private Long tipoRetorno;

	@Column(name = "dt_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;

	@Column(name = "cd_status")
	private Long codigoStatus;

}