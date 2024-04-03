package com.sjw.doran.orderservice.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeliveryTracking is a Querydsl query type for DeliveryTracking
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDeliveryTracking extends EntityPathBase<DeliveryTracking> {

    private static final long serialVersionUID = -1263270908L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeliveryTracking deliveryTracking = new QDeliveryTracking("deliveryTracking");

    public final StringPath contactNumber = createString("contactNumber");

    public final StringPath courier = createString("courier");

    public final QDelivery delivery;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> postDateTime = createDateTime("postDateTime", java.time.LocalDateTime.class);

    public final StringPath postLocation = createString("postLocation");

    public QDeliveryTracking(String variable) {
        this(DeliveryTracking.class, forVariable(variable), INITS);
    }

    public QDeliveryTracking(Path<? extends DeliveryTracking> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeliveryTracking(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeliveryTracking(PathMetadata metadata, PathInits inits) {
        this(DeliveryTracking.class, metadata, inits);
    }

    public QDeliveryTracking(Class<? extends DeliveryTracking> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.delivery = inits.isInitialized("delivery") ? new QDelivery(forProperty("delivery"), inits.get("delivery")) : null;
    }

}

