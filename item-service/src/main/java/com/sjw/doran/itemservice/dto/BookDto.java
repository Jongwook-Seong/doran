package com.sjw.doran.itemservice.dto;

import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto extends ItemDto {

    private String author;
    private String isbn;
    private int pages;
    private Date publicationDate;
    private String contentsTable;
    private String bookReview;

    public static BookDto getInstanceForCreate(BookCreateRequest bookCreateRequest) {
        BookDto bookDto = BookDto.builder()
//                .itemUuid(UUID.randomUUID().toString())
//                .itemName(bookCreateRequest.getItemName())
//                .price(bookCreateRequest.getPrice())
//                .stockQuantity(bookCreateRequest.getStockQuantity())
//                .category(Category.BOOK)
                .author(bookCreateRequest.getAuthor())
                .isbn(bookCreateRequest.getIsbn())
                .pages(bookCreateRequest.getPages())
                .publicationDate(bookCreateRequest.getPublicationDate())
                .contentsTable(bookCreateRequest.getContentsTable())
                .bookReview(bookCreateRequest.getBookReview())
                .build();

        bookDto.setItemUuid( UUID.randomUUID().toString() );
        bookDto.setItemName( bookCreateRequest.getItemName() );
        bookDto.setPrice( bookCreateRequest.getPrice() );
        bookDto.setStockQuantity( bookCreateRequest.getStockQuantity() );

        return bookDto;
    }
}
