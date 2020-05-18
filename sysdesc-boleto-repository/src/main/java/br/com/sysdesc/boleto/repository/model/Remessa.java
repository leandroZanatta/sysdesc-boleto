package br.com.sysdesc.boleto.repository.model;

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
@Table(name = "tb_remessa")
@SequenceGenerator(name = "GEN_REMESSA", allocationSize = 1, sequenceName = "GEN_REMESSA")
public class Remessa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_REMESSA")
    @Column(name = "id_remessa")
    private Long idRemessa;

    @Column(name = "nr_banco")
    private Long numeroBanco;

    @Column(name = "nr_remessa")
    private Long numeroRemessa;

    @Column(name = "bl_arquivo")
    private byte[] arquivo;

    @Column(name = "bl_retorno")
    private byte[] retorno;

    @Column(name = "dt_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;

    @Column(name = "cd_status")
    private Long codigoStatus;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "remessa")
    private List<RemessaBoleto> remessaBoletos = new ArrayList<>();

    public void addBoleto(Boleto boleto) {

        RemessaBoleto remessaBoleto = new RemessaBoleto();
        remessaBoleto.setBoleto(boleto);
        remessaBoleto.setRemessa(this);

        remessaBoletos.add(remessaBoleto);
    }

}