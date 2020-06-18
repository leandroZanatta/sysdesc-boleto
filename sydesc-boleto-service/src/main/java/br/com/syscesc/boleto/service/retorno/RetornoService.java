package br.com.syscesc.boleto.service.retorno;

import java.util.List;

import br.com.sysdesc.boleto.repository.dao.RetornoDAO;
import br.com.sysdesc.boleto.repository.model.Retorno;
import br.com.sysdesc.util.vo.PesquisaRemessaRetornoVO;

public class RetornoService {

	private RetornoDAO retornoDAO = new RetornoDAO();

	public List<Retorno> pesquisarRetornos(PesquisaRemessaRetornoVO pesquisa) {

		return retornoDAO.pesquisarRetornos(pesquisa);
	}

	public void salvar(Retorno remessa) {

		retornoDAO.salvar(remessa);
	}

}
