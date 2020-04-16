package br.com.sysdesc.boleto.repository.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QConfiguracaoBoleto is a Querydsl query type for ConfiguracaoBoleto
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QConfiguracaoBoleto extends EntityPathBase<ConfiguracaoBoleto> {

    private static final long serialVersionUID = -60569273L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QConfiguracaoBoleto configuracaoBoleto = new QConfiguracaoBoleto("configuracaoBoleto");

    public final QBanco banco;

    public final ListPath<Boleto, QBoleto> boletos = this.<Boleto, QBoleto>createList("boletos", Boleto.class, QBoleto.class, PathInits.DIRECT2);

    public final StringPath cgc = createString("cgc");

    public final NumberPath<Long> codigoCarteira = createNumber("codigoCarteira", Long.class);

    public final StringPath flagTipoCliente = createString("flagTipoCliente");

    public final NumberPath<Long> idConfiguracaoBoleto = createNumber("idConfiguracaoBoleto", Long.class);

    public final StringPath nome = createString("nome");

    public final NumberPath<Long> nossoNumero = createNumber("nossoNumero", Long.class);

    public final StringPath numeroAgencia = createString("numeroAgencia");

    public final StringPath numeroConta = createString("numeroConta");

    public QConfiguracaoBoleto(String variable) {
        this(ConfiguracaoBoleto.class, forVariable(variable), INITS);
    }

    public QConfiguracaoBoleto(Path<? extends ConfiguracaoBoleto> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QConfiguracaoBoleto(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QConfiguracaoBoleto(PathMetadata<?> metadata, PathInits inits) {
        this(ConfiguracaoBoleto.class, metadata, inits);
    }

    public QConfiguracaoBoleto(Class<? extends ConfiguracaoBoleto> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.banco = inits.isInitialized("banco") ? new QBanco(forProperty("banco"), inits.get("banco")) : null;
    }

}

