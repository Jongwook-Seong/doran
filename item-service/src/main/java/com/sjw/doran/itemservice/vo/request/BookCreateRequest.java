package com.sjw.doran.itemservice.vo.request;

import com.sjw.doran.itemservice.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
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
