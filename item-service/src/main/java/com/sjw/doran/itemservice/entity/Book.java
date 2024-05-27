package com.sjw.doran.itemservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("BOOK")
public class Book extends Item {

    private String author;
    private String isbn;
    private int pages;
    private Date publicationDate;
    private String contentsTable;
    private String bookReview;

    @Builder
    public Book(String itemUuid, String itemName, int price, int stockQuantity, String itemImageUrl, Category category,
            String author, String isbn, int pages, Date publicationDate, String contentsTable, String bookReview) {
        super(itemUuid, itemName, price, stockQuantity, itemImageUrl, category);
        this.author = author;
        this.isbn = isbn;
        this.pages = pages;
        this.publicationDate = publicationDate;
        this.contentsTable = contentsTable;
        this.bookReview = bookReview;
    }
}
