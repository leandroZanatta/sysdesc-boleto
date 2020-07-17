package br.com.sysdesc.boletos.repository.model.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RemessaBoletoPk implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "cd_boleto")
	private Long codigoBoleto;

	@Column(name = "cd_remessa")
	private Long codigoRemessa;
}
