package br.com.sysdesc.boletos.repository.dao;

import static br.com.sysdesc.boleto.repository.model.QConfiguracaoRemessa.configuracaoRemessa;

import br.com.sysdesc.boletos.repository.model.ConfiguracaoRemessa;
import br.com.sysdesc.pesquisa.repository.dao.impl.PesquisableDAOImpl;

public class ConfiguracaoRemessaDAO extends PesquisableDAOImpl<ConfiguracaoRemessa> {

    private static final long serialVersionUID = 1L;

    public ConfiguracaoRemessaDAO() {
        super(configuracaoRemessa, configuracaoRemessa.numeroBanco);
    }

    public Long buscarCodigoRemessaPorBanco(Long numeroBanco) {

        Long codigoRemessa = from().where(configuracaoRemessa.numeroBanco.eq(numeroBanco)).singleResult(configuracaoRemessa.numeroRemessa);

        if (codigoRemessa == null) {

            codigoRemessa = 0L;
        }
        return ++codigoRemessa;
    }

}
