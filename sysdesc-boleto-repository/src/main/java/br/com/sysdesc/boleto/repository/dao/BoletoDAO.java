package br.com.sysdesc.boleto.repository.dao;

import static br.com.sysdesc.boleto.repository.model.QBoleto.boleto;
import static br.com.sysdesc.boleto.repository.model.QRemessaBoleto.remessaBoleto;

import java.util.List;

import br.com.sysdesc.boleto.repository.model.Boleto;
import br.com.sysdesc.util.dao.AbstractGenericDAO;

public class BoletoDAO extends AbstractGenericDAO<Boleto> {

    private static final long serialVersionUID = 1L;

    public BoletoDAO() {
        super(boleto, boleto.idBoleto);
    }

    public List<Boleto> buscarBoletosParaEnvio(Long numeroBanco) {

        return from().where(boleto.numeroBanco.eq(numeroBanco)
                .and(subQuery().from(remessaBoleto).where(boleto.idBoleto.eq(remessaBoleto.id.codigoBoleto)).notExists())).list(boleto);
    }

}
