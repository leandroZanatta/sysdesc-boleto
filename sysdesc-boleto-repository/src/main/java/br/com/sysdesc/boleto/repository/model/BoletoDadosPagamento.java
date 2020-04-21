package br.com.sysdesc.boleto.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_boletodadospagamento")
public class BoletoDadosPagamento implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cd_boleto", referencedColumnName = "id_boleto")
    private Boleto boleto;

    @Column(name = "cd_protesto", nullable = false)
    private Long codigoProtesto;

    @Column(name = "nr_diasprotesto")
    private Long diasProtesto;

    @Column(name = "tx_modalidade")
    private String modalidade;

    @Column(name = "cd_carteira")
    private Long codigoCarteira;

    @Column(name = "cd_jurosmora")
    private Long codigoJurosMora;

    @Column(name = "dt_jurosmora")
    @Temporal(TemporalType.DATE)
    private Date dataJurosMora;

    @Column(name = "vl_jurosmora")
    private BigDecimal valorJurosMora;

    @Column(name = "cd_multa")
    private Long codigoMulta;

    @Column(name = "dt_multa")
    @Temporal(TemporalType.DATE)
    private Date dataMulta;

    @Column(name = "vl_multa")
    private BigDecimal valorMulta;

    @Column(name = "dt_limitepagamento")
    @Temporal(TemporalType.DATE)
    private Date dataLimitePagamento;

}