package br.com.sysdesc.boletos.service.remessa;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jrimum.texgit.FlatFile;
import org.jrimum.texgit.Record;

import br.com.sysdesc.boletos.repository.dao.BancoDAO;
import br.com.sysdesc.boletos.repository.dao.BoletoDAO;
import br.com.sysdesc.boletos.repository.dao.ConfiguracaoRemessaDAO;
import br.com.sysdesc.boletos.repository.dao.RemessaDAO;
import br.com.sysdesc.boletos.repository.model.Banco;
import br.com.sysdesc.boletos.repository.model.Boleto;
import br.com.sysdesc.boletos.repository.model.ConfiguracaoRemessa;
import br.com.sysdesc.boletos.repository.model.Remessa;
import br.com.sysdesc.boletos.service.factory.RemessaFactory;
import br.com.sysdesc.util.classes.ListUtil;
import br.com.sysdesc.util.enumeradores.TipoStatusBoletoEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusRemessaEnum;
import br.com.sysdesc.util.vo.PesquisaRemessaRetornoVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemessaService {

    private RemessaFactory remessaFactory = new RemessaFactory();

    private BoletoDAO boletoDAO = new BoletoDAO();

    private BancoDAO bancoDAO = new BancoDAO();

    private ConfiguracaoRemessaDAO configuracaoRemessaDAO = new ConfiguracaoRemessaDAO();

    private RemessaDAO remessaDAO = new RemessaDAO();

    public void processarRemessaBancosSuportados() {

        bancoDAO.buscarBancosSuporteBoleto().forEach(this::gerarRemessa);
    }

    private void gerarRemessa(Banco banco) {

        List<Boleto> boletosParaEnviar = boletoDAO.buscarBoletosParaEnvio(banco.getNumeroBanco());

        if (!ListUtil.isNullOrEmpty(boletosParaEnviar)) {
            try {

                Long codigoRemessa = configuracaoRemessaDAO.buscarCodigoRemessaPorBanco(banco.getNumeroBanco());

                FlatFile<Record> records = remessaFactory.getRemessaBuilder(banco).build(boletosParaEnviar, codigoRemessa);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                IOUtils.writeLines(records.write(), null, byteArrayOutputStream, StandardCharsets.UTF_8);

                salvarRemessa(banco.getNumeroBanco(), codigoRemessa, byteArrayOutputStream, boletosParaEnviar);

                salvarConfiguracaoRemessa(banco.getNumeroBanco(), codigoRemessa);

                boletosParaEnviar.forEach(boleto -> boleto.setCodigoStatus(TipoStatusBoletoEnum.REMESSA_GERADA.getCodigo()));

                boletoDAO.salvar(boletosParaEnviar);

            } catch (Exception e) {

                log.error("Erro Gerando Remessa", e);
            }
        }
    }

    private void salvarConfiguracaoRemessa(Long numeroBanco, Long codigoRemessa) {

        ConfiguracaoRemessa configuracaoRemessa = configuracaoRemessaDAO.obterPorId(numeroBanco);

        if (configuracaoRemessa == null) {
            configuracaoRemessa = new ConfiguracaoRemessa();
            configuracaoRemessa.setNumeroBanco(numeroBanco);
        }

        configuracaoRemessa.setNumeroRemessa(codigoRemessa);

        configuracaoRemessaDAO.salvar(configuracaoRemessa);
    }

    private void salvarRemessa(Long numeroBanco, Long codigoRemessa, ByteArrayOutputStream stringOuputStream, List<Boleto> boletosParaEnviar) {

        Remessa remessa = new Remessa();
        remessa.setArquivo(stringOuputStream.toByteArray());
        remessa.setCodigoStatus(TipoStatusRemessaEnum.GERADO.getCodigo());
        remessa.setDataCadastro(new Date());
        remessa.setNumeroBanco(numeroBanco);
        remessa.setNumeroRemessa(codigoRemessa);
        boletosParaEnviar.forEach(remessa::addBoleto);

        remessaDAO.salvar(remessa);
    }

    public List<Remessa> pesquisarRemessas(PesquisaRemessaRetornoVO pesquisa) {

        return remessaDAO.pesquisarRemessas(pesquisa);
    }

    public void salvar(Remessa remessa) {

        remessaDAO.salvar(remessa);
    }

}
