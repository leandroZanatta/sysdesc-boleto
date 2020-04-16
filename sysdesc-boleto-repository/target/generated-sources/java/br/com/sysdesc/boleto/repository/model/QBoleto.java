package br.com.sysdesc.boleto.repository.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QBoleto is a Querydsl query type for Boleto
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QBoleto extends EntityPathBase<Boleto> {

    private static final long serialVersionUID = 562388280L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoleto boleto = new QBoleto("boleto");

    public final ArrayPath<byte[], Byte> arquivo = createArray("arquivo", byte[].class);

    public final StringPath codigoBarras = createString("codigoBarras");

    public final NumberPath<Long> codigoStatus = createNumber("codigoStatus", Long.class);

    public final QConfiguracaoBoleto configuracaoBoleto;

    public final DateTimePath<java.util.Date> dataCadastro = createDateTime("dataCadastro", java.util.Date.class);

    public final DatePath<java.util.Date> dataVencimento = createDate("dataVencimento", java.util.Date.class);

    public final NumberPath<Long> idBoleto = createNumber("idBoleto", Long.class);

    public final StringPath nossoNumero = createString("nossoNumero");

    public final NumberPath<Long> numeroBanco = createNumber("numeroBanco", Long.class);

    public final NumberPath<java.math.BigDecimal> valorBoleto = createNumber("valorBoleto", java.math.BigDecimal.class);

    public QBoleto(String variable) {
        this(Boleto.class, forVariable(variable), INITS);
    }

    public QBoleto(Path<? extends Boleto> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QBoleto(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QBoleto(PathMetadata<?> metadata, PathInits inits) {
        this(Boleto.class, metadata, inits);
    }

    public QBoleto(Class<? extends Boleto> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.configuracaoBoleto = inits.isInitialized("configuracaoBoleto") ? new QConfiguracaoBoleto(forProperty("configuracaoBoleto"), inits.get("configuracaoBoleto")) : null;
    }

}

