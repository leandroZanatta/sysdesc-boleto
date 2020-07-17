package br.com.sysdesc.boletos.repository.dao;

import static br.com.sysdesc.boleto.repository.model.QRemessa.remessa;

import java.util.List;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;

import br.com.sysdesc.boletos.repository.model.Remessa;
import br.com.sysdesc.pesquisa.repository.dao.impl.PesquisableDAOImpl;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.classes.LongUtil;
import br.com.sysdesc.util.vo.PesquisaRemessaRetornoVO;

public class RemessaDAO extends PesquisableDAOImpl<Remessa> {

	private static final long serialVersionUID = 1L;

	public RemessaDAO() {
		super(remessa, remessa.idRemessa);
	}

	public List<Remessa> pesquisarRemessas(PesquisaRemessaRetornoVO pesquisa) {
		JPAQuery query = from();

		BooleanBuilder booleanBuilder = new BooleanBuilder();

		if (!LongUtil.isNullOrZero(pesquisa.getNumeroDocumento())) {
			booleanBuilder.and(remessa.numeroRemessa.eq(pesquisa.getNumeroDocumento()));
		}

		if (!LongUtil.isNullOrZero(pesquisa.getCodigoBanco())) {
			booleanBuilder.and(remessa.numeroBanco.eq(pesquisa.getCodigoBanco()));
		}

		if (!LongUtil.isNullOrZero(pesquisa.getCodigoStatus())) {
			booleanBuilder.and(remessa.codigoStatus.eq(pesquisa.getCodigoStatus()));
		}

		if (pesquisa.getDataInicial() != null || pesquisa.getDataFinal() != null) {

			if (pesquisa.getDataInicial() != null && pesquisa.getDataFinal() != null) {

				booleanBuilder.and(remessa.dataCadastro.between(pesquisa.getDataInicial(), DateUtil.getFinalDate(pesquisa.getDataFinal())));
			} else if (pesquisa.getDataInicial() != null) {

				booleanBuilder.and(remessa.dataCadastro.after(pesquisa.getDataInicial()));
			} else {
				booleanBuilder.and(remessa.dataCadastro.before(DateUtil.getFinalDate(pesquisa.getDataFinal())));
			}
		}

		if (booleanBuilder.hasValue()) {

			query.where(booleanBuilder);
		}

		return query.list(remessa);
	}

}
