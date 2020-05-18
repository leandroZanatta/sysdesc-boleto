package br.com.sysdesc.boleto.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_configuracaoremessa")
public class ConfiguracaoRemessa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "nr_banco")
    private Long numeroBanco;

    @Column(name = "nr_remessa")
    private Long numeroRemessa;

}