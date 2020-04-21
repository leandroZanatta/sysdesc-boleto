package br.com.sysdesc.boletos.util.model.sicoob;

import br.com.sysdesc.boletos.util.anotations.XmlAnotation;
import br.com.sysdesc.boletos.util.model.InnerRecordModel;
import lombok.Data;

@Data
public class DetalheQSicoob implements InnerRecordModel {

    @XmlAnotation(name = "numeroLote", type = Long.class, size = 4)
    private Long numeroLote;

    @XmlAnotation(name = "numeroRegistro", type = Long.class, size = 5)
    private Long numeroRegistro;

    @XmlAnotation(name = "codigoMovimento", type = Long.class, size = 2)
    private Long codigoMovimento;

    @XmlAnotation(name = "tipoCliente", type = Long.class, size = 1)
    private Long tipoCliente;

    @XmlAnotation(name = "cgcCliente", type = Long.class, size = 15)
    private Long cgcCliente;

    @XmlAnotation(name = "nome", type = String.class, size = 40)
    private String nome;

    @XmlAnotation(name = "endereco", type = String.class, size = 40)
    private String endereco;

    @XmlAnotation(name = "bairro", type = String.class, size = 15)
    private String bairro;

    @XmlAnotation(name = "cep", type = Long.class, size = 5)
    private Long cep;

    @XmlAnotation(name = "sufixoCep", type = Long.class, size = 3)
    private Long sufixoCep;

    @XmlAnotation(name = "cidade", type = String.class, size = 15)
    private String cidade;

    @XmlAnotation(name = "uf", type = String.class, size = 2)
    private String uf;

    @XmlAnotation(name = "tipoSacadorAvalista", type = Long.class, size = 1)
    private Long tipoSacadorAvalista = 0L;

    @XmlAnotation(name = "cgcSacadorAvalista", type = Long.class, size = 15)
    private Long cgcSacadorAvalista = 0L;

    @XmlAnotation(name = "nomeSacadorAvalista", type = String.class, size = 40)
    private String nomeSacadorAvalista = "";

    @XmlAnotation(name = "bancoCompensacao", type = Long.class, size = 3)
    private Long bancoCompensacao;

    @XmlAnotation(name = "bancoCorrespondente", type = String.class, size = 20)
    private String bancoCorrespondente;

    @Override
    public String getRecordName() {

        return "DetalheSegmentoQ";
    }

}
