package br.com.sysdesc.boleto.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_banco")
@SequenceGenerator(name = "GEN_BANCO", allocationSize = 1, sequenceName = "GEN_BANCO")
public class Banco implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_BANCO")
    @Column(name = "id_banco")
    private Long idBanco;

    @Column(name = "nr_banco")
    private Long numeroBanco;

    @Column(name = "nr_agencia")
    private String numeroAgencia;

    @Column(name = "nr_conta")
    private String numeroConta;

    @Column(name = "nr_situacao")
    private Long situacao;

    @OneToOne(mappedBy = "banco", fetch = FetchType.LAZY, optional = true)
    private ConfiguracaoBoleto configuracaoBoleto;

}