package br.com.syscesc.boleto.service.boleto.sicoob;

import static br.com.sysdesc.util.classes.StringUtil.concat;
import static br.com.sysdesc.util.classes.StringUtil.formatarNumero;
import static br.com.sysdesc.util.classes.StringUtil.padLeft;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;

import br.com.boletos.service.BancoBoletoService;
import br.com.sysdesc.boleto.repository.model.ConfiguracaoBoleto;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.resources.Configuracoes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoletoSicoobService implements BancoBoletoService {

    protected final ConfiguracaoBoleto configuracaoBoleto;

    public BoletoSicoobService(ConfiguracaoBoleto configuracaoBoleto) {

        this.configuracaoBoleto = configuracaoBoleto;
    }

    @Override
    public ContaBancaria createContaBancaria() {

        String agencia = StringUtil.formatarNumero(configuracaoBoleto.getNumeroAgencia());

        Integer numeroAgencia = Integer.valueOf(agencia.substring(0, agencia.length() - 1));
        Integer conta = Integer.valueOf(StringUtil.formatarNumero(configuracaoBoleto.getNumeroConta()));

        String digitoAgencia = agencia.substring(agencia.length() - 1, agencia.length());

        ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCOOB.create());
        contaBancaria.setAgencia(new Agencia(numeroAgencia, digitoAgencia));
        contaBancaria.setNumeroDaConta(new NumeroDaConta(conta));
        contaBancaria.setCarteira(new Carteira(configuracaoBoleto.getCodigoCarteira().intValue()));

        Image image = null;

        try {

            image = ImageIO.read(new File(Configuracoes.FOLDER_IMAGE + File.separator + "logo-sicoob.png"));

            contaBancaria.getBanco().setImgLogo(image);
        } catch (IOException e) {

            log.warn("NÃO FOI POSSIVEL GERAR A LOGO DO SICOOB PARA GERAÇÃO DO BOLETO", e);
        }

        return contaBancaria;
    }

    @Override
    public String gerarNossoNumero() {

        String inicialNossoNumero = padLeft(this.configuracaoBoleto.getNossoNumero().toString(), 7L);
        String agencia = padLeft(formatarNumero(this.configuracaoBoleto.getNumeroAgencia()), 5L);

        String nossoNumero = concat(agencia.substring(0, 4), padLeft(formatarNumero(configuracaoBoleto.getNumeroConta()), 10L), inicialNossoNumero);

        return concat(inicialNossoNumero, calcularDigitoVerificador(nossoNumero));
    }

    private String calcularDigitoVerificador(String nossoNumero) {

        List<Integer> numerosMultiplicacao = Arrays.asList("319731973197319731973".split("")).stream().mapToInt(Integer::valueOf).boxed()
                .collect(Collectors.toList());

        List<Integer> numerosBase = Arrays.asList(nossoNumero.split("")).stream().mapToInt(Integer::valueOf).boxed().collect(Collectors.toList());

        Double valor = 0.0;

        for (int i = 0; i < numerosBase.size(); i++) {
            valor += numerosBase.get(i) * numerosMultiplicacao.get(i);
        }

        Integer resto = (int) (valor % 11);

        Integer digito = (11 - resto);

        return resto <= 1 ? "0" : digito.toString();
    }

    @Override
    public Cedente gerarCedente() {

        return new Cedente(configuracaoBoleto.getNome(), configuracaoBoleto.getCgc());
    }

}
