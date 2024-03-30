package com.sjw.doran.itemservice.dto;

import com.sjw.doran.itemservice.entity.Category;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private String itemUuid;
    private String itemName;
    private int price;
    private int stockQuantity;
    private Category category;

    private String author;
    private String isbn;
    private int pages;
    private Date publicationDate;
    private String contentsTable;
    private String bookReview;

    public static BookDto getInstanceForCreate(BookCreateRequest bookCreateRequest) {
        return BookDto.builder()
                .itemUuid(UUID.randomUUID().toString())
                .itemName(bookCreateRequest.getItemName())
                .price(bookCreateRequest.getPrice())
                .stockQuantity(bookCreateRequest.getStockQuantity())
                .category(Category.BOOK)
                .author(bookCreateRequest.getAuthor())
                .isbn(bookCreateRequest.getIsbn())
                .pages(bookCreateRequest.getPages())
                .publicationDate(bookCreateRequest.getPublicationDate())
                .contentsTable(bookCreateRequest.getContentsTable())
                .bookReview(bookCreateRequest.getBookReview())
                .build();
    }
}
