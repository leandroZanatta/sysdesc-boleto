package br.com.sysdesc.boleto.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_boleto")
@SequenceGenerator(name = "GEN_BOLETO", allocationSize = 1, sequenceName = "GEN_BOLETO")
public class Boleto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_BOLETO")
    @Column(name = "id_boleto")
    private Long idBoleto;

    @ManyToOne
    @JoinColumn(name = "cd_configuracaoboleto")
    private ConfiguracaoBoleto configuracaoBoleto;

    @Column(name = "fl_aceite")
    private String aceite;

    @Column(name = "nr_especietitulo")
    private Long especieTitulo;

    @Column(name = "cd_numerobanco")
    private Long numeroBanco;

    @Column(name = "cd_nossonumero")
    private String nossoNumero;

    @Column(name = "cd_codigobarras")
    private String codigoBarras;

    @Column(name = "vl_boleto")
    private BigDecimal valorBoleto;

    @Column(name = "dt_vencimento")
    @Temporal(TemporalType.DATE)
    private Date dataVencimento;

    @Column(name = "dt_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;

    @Column(name = "bl_arquivo")
    private byte[] arquivo;

    @Column(name = "cd_status")
    private Long codigoStatus;

    @Column(name = "cd_statusemail")
    private Long codigoStatusEmail;

    @OneToOne(mappedBy = "boleto", cascade = CascadeType.ALL)
    private BoletoDadosCliente boletoDadosCliente;

    @OneToOne(mappedBy = "boleto", cascade = CascadeType.ALL)
    private BoletoDadosSacadorAvalista boletoDadosSacadorAvalista;

    @OneToOne(mappedBy = "boleto", cascade = CascadeType.ALL)
    private BoletoDadosPagamento boletoDadosPagamento;

    @OneToOne(mappedBy = "boleto")
    private RemessaBoleto remessaBoleto;

}