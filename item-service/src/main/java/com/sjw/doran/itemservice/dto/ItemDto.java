package com.sjw.doran.itemservice.dto;

import com.sjw.doran.itemservice.entity.Category;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@Builder
@RequiredArgsConstructor
public class ItemDto {

    private String itemUuid;
    private String itemName;
    private int price;
    private int stockQuantity;
    private String itemImageUrl;
    private Category category;

//    private String author;
//    private String isbn;
//    private int pages;
//    private Date publicationDate;
//    private String contentsTable;
//    private String bookReview;
//
//    private String artist;
//    private String explanation;
//    private String workSize;
//    private int productionYear;

//    @Builder
//    public ItemDto(String itemUuid, String itemName, int price, int stockQuantity, String itemImageUrl, Category category) {
//        this.itemUuid = itemUuid;
//        this.itemName = itemName;
//        this.price = price;
//        this.stockQuantity = stockQuantity;
//        this.itemImageUrl = itemImageUrl;
//        this.category = category;
//    }
}
