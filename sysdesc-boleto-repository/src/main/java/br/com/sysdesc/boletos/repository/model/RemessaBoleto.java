package br.com.sysdesc.boletos.repository.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.sysdesc.boletos.repository.model.pk.RemessaBoletoPk;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_remessaboleto")
public class RemessaBoleto implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private RemessaBoletoPk id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cd_remessa", referencedColumnName = "id_remessa")
    @MapsId("codigoRemessa")
    private Remessa remessa;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cd_boleto", referencedColumnName = "id_boleto")
    @MapsId("codigoBoleto")
    private Boleto boleto;
}