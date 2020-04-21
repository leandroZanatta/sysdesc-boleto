package br.com.boleto.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.syscesc.boleto.service.boleto.sicoob.BoletoSicoobService;
import br.com.sysdesc.boleto.repository.model.ConfiguracaoBoleto;

@RunWith(MockitoJUnitRunner.class)
public class BoletoSicoobServiceTest {

    private BoletoSicoobService boletoSicoobService;

    private ConfiguracaoBoleto configuracaoBoleto = new ConfiguracaoBoleto();

    @Before
    public void initialize() {
        configuracaoBoleto.setCodigoCarteira(1L);
        configuracaoBoleto.setModalidade("01");

        boletoSicoobService = Mockito.spy(new BoletoSicoobService(configuracaoBoleto));
    }

    @Test
    public void testarNossoNumero() throws Exception {
        configuracaoBoleto.setNumeroAgencia("0001-4");
        configuracaoBoleto.setNumeroConta("1-9");
        configuracaoBoleto.setNossoNumero(21L);

        assertEquals("00000218", boletoSicoobService.gerarNossoNumero());
    }

    @Test
    public void testarNossoNumero2() throws Exception {
        configuracaoBoleto.setNumeroAgencia("1001-4");
        configuracaoBoleto.setNumeroConta("216844-5");
        configuracaoBoleto.setNossoNumero(13L);

        assertEquals("00000130", boletoSicoobService.gerarNossoNumero());
    }
}
