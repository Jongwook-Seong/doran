package com.sjw.doran.itemservice.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = 820559027L;

    public static final QBook book = new QBook("book");

    public final QItem _super = new QItem(this);

    public final StringPath author = createString("author");

    public final StringPath bookReview = createString("bookReview");

    //inherited
    public final ListPath<Category, QCategory> categories = _super.categories;

    public final StringPath contentsTable = createString("contentsTable");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath isbn = createString("isbn");

    //inherited
    public final StringPath itemImageUrl = _super.itemImageUrl;

    //inherited
    public final StringPath itemName = _super.itemName;

    //inherited
    public final StringPath itemUuid = _super.itemUuid;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Integer> pages = createNumber("pages", Integer.class);

    //inherited
    public final NumberPath<Integer> price = _super.price;

    public final DatePath<java.time.LocalDate> publicationDate = createDate("publicationDate", java.time.LocalDate.class);

    //inherited
    public final NumberPath<Integer> stockQuantity = _super.stockQuantity;

    public QBook(String variable) {
        super(Book.class, forVariable(variable));
    }

    public QBook(Path<? extends Book> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBook(PathMetadata metadata) {
        super(Book.class, metadata);
    }

}

