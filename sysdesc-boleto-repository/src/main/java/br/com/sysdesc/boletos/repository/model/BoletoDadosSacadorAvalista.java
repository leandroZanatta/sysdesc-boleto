package br.com.sysdesc.boletos.repository.model;

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
@Table(name = "tb_boletodadossacadoravalista")
public class BoletoDadosSacadorAvalista implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cd_boleto", referencedColumnName = "id_boleto")
    private Boleto boleto;

    @Column(name = "fl_tipocliente")
    private String flagTipoCliente;

    @Column(name = "tx_cgc")
    private String cgc;

    @Column(name = "tx_nome")
    private String nome;

}