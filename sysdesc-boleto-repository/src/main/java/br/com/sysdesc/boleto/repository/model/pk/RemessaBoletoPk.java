package br.com.sysdesc.boleto.repository.model.pk;

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
public class RemessaBoletoPk {

    @Column(name = "cd_boleto")
    private Long codigoBoleto;

    @Column(name = "cd_remessa")
    private Long codigoRemessa;
}
