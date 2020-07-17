package br.com.sysdesc.boletos.repository.dao;

import static br.com.sysdesc.boleto.repository.model.QRetorno.retorno;

import java.util.List;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;

import br.com.sysdesc.boletos.repository.model.Retorno;
import br.com.sysdesc.pesquisa.repository.dao.impl.PesquisableDAOImpl;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.classes.LongUtil;
import br.com.sysdesc.util.vo.PesquisaRemessaRetornoVO;

public class RetornoDAO extends PesquisableDAOImpl<Retorno> {

	private static final long serialVersionUID = 1L;

	public RetornoDAO() {
		super(retorno, retorno.idRetorno);
	}

	public boolean retornoImportado(Long numeroBanco, Long numeroRetono) {

		return from().where(retorno.numeroBanco.eq(numeroBanco).and(retorno.numeroRetono.eq(numeroRetono))).exists();
	}

	public List<Retorno> pesquisarRetornos(PesquisaRemessaRetornoVO pesquisa) {
		JPAQuery query = from();

		BooleanBuilder booleanBuilder = new BooleanBuilder();

		if (!LongUtil.isNullOrZero(pesquisa.getNumeroDocumento())) {
			booleanBuilder.and(retorno.numeroRetono.eq(pesquisa.getNumeroDocumento()));
		}

		if (!LongUtil.isNullOrZero(pesquisa.getCodigoBanco())) {
			booleanBuilder.and(retorno.numeroBanco.eq(pesquisa.getCodigoBanco()));
		}

		if (!LongUtil.isNullOrZero(pesquisa.getCodigoStatus())) {
			booleanBuilder.and(retorno.codigoStatus.eq(pesquisa.getCodigoStatus()));
		}

		if (pesquisa.getDataInicial() != null || pesquisa.getDataFinal() != null) {

			if (pesquisa.getDataInicial() != null && pesquisa.getDataFinal() != null) {

				booleanBuilder.and(retorno.dataCadastro.between(pesquisa.getDataInicial(), DateUtil.getFinalDate(pesquisa.getDataFinal())));
			} else if (pesquisa.getDataInicial() != null) {

				booleanBuilder.and(retorno.dataCadastro.after(pesquisa.getDataInicial()));
			} else {
				booleanBuilder.and(retorno.dataCadastro.before(DateUtil.getFinalDate(pesquisa.getDataFinal())));
			}
		}

		if (booleanBuilder.hasValue()) {

			query.where(booleanBuilder);
		}

		return query.list(retorno);
	}

}
