package com.sjw.doran.itemservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("BOOK")
public class Book extends Item {

    private String author;
    private String isbn;
    private int pages;
    private LocalDate publicationDate;
    private String contentsTable;
    private String bookReview;
}
