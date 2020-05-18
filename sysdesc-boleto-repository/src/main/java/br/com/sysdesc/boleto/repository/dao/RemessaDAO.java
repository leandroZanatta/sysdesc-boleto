package br.com.sysdesc.boleto.repository.dao;

import static br.com.sysdesc.boleto.repository.model.QRemessa.remessa;

import br.com.sysdesc.boleto.repository.model.Remessa;
import br.com.sysdesc.pesquisa.repository.dao.impl.PesquisableDAOImpl;

public class RemessaDAO extends PesquisableDAOImpl<Remessa> {

    private static final long serialVersionUID = 1L;

    public RemessaDAO() {
        super(remessa, remessa.idRemessa);
    }

}
