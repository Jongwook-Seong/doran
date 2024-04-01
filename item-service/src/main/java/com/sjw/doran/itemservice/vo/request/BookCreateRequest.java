package com.sjw.doran.itemservice.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateRequest {

    private String itemName;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
    private int pages;
    private Date publicationDate;
    private String contentsTable;
    private String bookReview;
}
