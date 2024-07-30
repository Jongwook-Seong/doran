package com.sjw.doran.requestdatagenerator.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("BOOK")
public class Book extends Item {

    private String author;
    private String isbn;
    private int pages;
    @Column(name = "publication_date")
    private Date publicationDate;
    @Column(name = "contents_table")
    private String contentsTable;
    @Column(name = "book_review")
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

    @Builder
    public Book(Long id, String itemUuid, String itemName, int price, int stockQuantity, String itemImageUrl, Category category,
                String author, String isbn, int pages, Date publicationDate, String contentsTable, String bookReview) {
        super(id, itemUuid, itemName, price, stockQuantity, itemImageUrl, category);
        this.author = author;
        this.isbn = isbn;
        this.pages = pages;
        this.publicationDate = publicationDate;
        this.contentsTable = contentsTable;
        this.bookReview = bookReview;
    }
}
