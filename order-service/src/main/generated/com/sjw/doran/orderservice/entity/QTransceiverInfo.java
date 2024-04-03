package com.sjw.doran.orderservice.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTransceiverInfo is a Querydsl query type for TransceiverInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QTransceiverInfo extends BeanPath<TransceiverInfo> {

    private static final long serialVersionUID = 1886037273L;

    public static final QTransceiverInfo transceiverInfo = new QTransceiverInfo("transceiverInfo");

    public final StringPath ordererName = createString("ordererName");

    public final StringPath receiverName = createString("receiverName");

    public final StringPath receiverPhoneNumber = createString("receiverPhoneNumber");

    public QTransceiverInfo(String variable) {
        super(TransceiverInfo.class, forVariable(variable));
    }

    public QTransceiverInfo(Path<? extends TransceiverInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTransceiverInfo(PathMetadata metadata) {
        super(TransceiverInfo.class, metadata);
    }

}

