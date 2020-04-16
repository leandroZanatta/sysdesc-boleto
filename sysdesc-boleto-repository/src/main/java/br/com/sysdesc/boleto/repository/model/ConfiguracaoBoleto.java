package br.com.sysdesc.boleto.repository.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_configuracaoboleto")
@SequenceGenerator(name = "GEN_CONFIGURACAOBOLETO", allocationSize = 1, sequenceName = "GEN_CONFIGURACAOBOLETO")
public class ConfiguracaoBoleto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_CONFIGURACAOBOLETO")
    @Column(name = "id_configuracaoboleto")
    private Long idConfiguracaoBoleto;

    @OneToOne
    @JoinColumn(name = "cd_banco")
    private Banco banco;

    @Column(name = "fl_tipocliente")
    private String flagTipoCliente;

    @Column(name = "cd_cgc")
    private String cgc;

    @Column(name = "tx_nome")
    private String nome;

    @Column(name = "cd_carteira")
    private Long codigoCarteira;

    @Column(name = "nr_agencia")
    private String numeroAgencia;

    @Column(name = "nr_conta")
    private String numeroConta;

    @Column(name = "nr_nossonumero")
    private Long nossoNumero;

    @OneToMany(mappedBy = "configuracaoBoleto")
    private List<Boleto> boletos;

}