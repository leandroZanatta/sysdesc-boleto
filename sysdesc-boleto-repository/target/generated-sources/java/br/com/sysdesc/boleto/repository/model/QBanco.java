package br.com.sysdesc.boleto.repository.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QBanco is a Querydsl query type for Banco
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QBanco extends EntityPathBase<Banco> {

    private static final long serialVersionUID = 1264652324L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBanco banco = new QBanco("banco");

    public final QConfiguracaoBoleto configuracaoBoleto;

    public final NumberPath<Long> idBanco = createNumber("idBanco", Long.class);

    public final StringPath numeroAgencia = createString("numeroAgencia");

    public final NumberPath<Long> numeroBanco = createNumber("numeroBanco", Long.class);

    public final StringPath numeroConta = createString("numeroConta");

    public final NumberPath<Long> situacao = createNumber("situacao", Long.class);

    public QBanco(String variable) {
        this(Banco.class, forVariable(variable), INITS);
    }

    public QBanco(Path<? extends Banco> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QBanco(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QBanco(PathMetadata<?> metadata, PathInits inits) {
        this(Banco.class, metadata, inits);
    }

    public QBanco(Class<? extends Banco> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.configuracaoBoleto = inits.isInitialized("configuracaoBoleto") ? new QConfiguracaoBoleto(forProperty("configuracaoBoleto"), inits.get("configuracaoBoleto")) : null;
    }

}

