package com.sjw.doran.itemservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("BOOK")
public class Book extends Item {

    private String author;
    private String isbn;
    private int pages;
    private Date publicationDate;
    private String contentsTable;
    private String bookReview;
}
