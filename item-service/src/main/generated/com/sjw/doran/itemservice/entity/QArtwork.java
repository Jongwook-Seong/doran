package com.sjw.doran.itemservice.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QArtwork is a Querydsl query type for Artwork
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArtwork extends EntityPathBase<Artwork> {

    private static final long serialVersionUID = 1818561002L;

    public static final QArtwork artwork = new QArtwork("artwork");

    public final QItem _super = new QItem(this);

    public final StringPath artist = createString("artist");

    //inherited
    public final EnumPath<Category> category = _super.category;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath explanation = createString("explanation");

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath itemImageUrl = _super.itemImageUrl;

    //inherited
    public final StringPath itemName = _super.itemName;

    //inherited
    public final StringPath itemUuid = _super.itemUuid;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final NumberPath<Integer> price = _super.price;

    public final NumberPath<Integer> productionYear = createNumber("productionYear", Integer.class);

    //inherited
    public final NumberPath<Integer> stockQuantity = _super.stockQuantity;

    public final StringPath workSize = createString("workSize");

    public QArtwork(String variable) {
        super(Artwork.class, forVariable(variable));
    }

    public QArtwork(Path<? extends Artwork> path) {
        super(path.getType(), path.getMetadata());
    }

    public QArtwork(PathMetadata metadata) {
        super(Artwork.class, metadata);
    }

}

