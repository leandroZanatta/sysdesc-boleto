package br.com.sysdesc.boletos.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;
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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    @Column(name = "tx_modalidade")
    private String modalidade;

    @Column(name = "cd_protesto")
    private Long codigoProtesto;

    @Column(name = "nr_diasprotesto")
    private Long diasProtesto;

    @Column(name = "cd_multa")
    private Long codigoMulta;

    @Column(name = "nr_diasmulta")
    private Long diasMulta;

    @Column(name = "vl_multa")
    private BigDecimal valorMulta;

    @Column(name = "cd_jurosmora")
    private Long codigoJurosMora;

    @Column(name = "nr_diasjurosmora")
    private Long diasJurosMora;

    @Column(name = "vl_jurosmora")
    private BigDecimal valorJurosMora;

    @Column(name = "nr_agencia")
    private String numeroAgencia;

    @Column(name = "nr_conta")
    private String numeroConta;

    @Column(name = "nr_nossonumero")
    private Long nossoNumero;

    @Column(name = "nr_diasmaximopagamento")
    private Long diasMaximoPagamento;

    @OneToMany(mappedBy = "configuracaoBoleto")
    private List<Boleto> boletos;

}