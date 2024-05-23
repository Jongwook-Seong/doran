package com.sjw.doran.itemservice.mapper;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;

import java.util.UUID;

public class BookMapperImpl implements BookMapper {

    @Override
    public Book toBook(BookDto bookDto) {

        if (bookDto == null) {
            return null;
        }

        return Book.builder()
                .itemUuid( bookDto.getItemUuid() )
                .itemName( bookDto.getItemName() )
                .price( bookDto.getPrice() )
                .stockQuantity( bookDto.getStockQuantity() )
                .itemImageUrl( bookDto.getItemImageUrl() )
                .category( bookDto.getCategory() )
                .author( bookDto.getAuthor() )
                .isbn( bookDto.getIsbn() )
                .pages( bookDto.getPages() )
                .publicationDate( bookDto.getPublicationDate() )
                .contentsTable( bookDto.getContentsTable() )
                .bookReview( bookDto.getBookReview() )
                .build();
    }

    @Override
    public BookDto toBookDto(BookCreateRequest request) {

        if (request == null) {
            return null;
        }

//        return BookDto.getInstanceForCreate(request);

        BookDto bookDto = BookDto.builder()
                .author( request.getAuthor() )
                .isbn( request.getIsbn() )
                .pages( request.getPages() )
                .publicationDate( request.getPublicationDate() )
                .contentsTable( request.getContentsTable() )
                .bookReview( request.getBookReview() )
                .build();

        bookDto.setItemUuid( UUID.randomUUID().toString() );
        bookDto.setItemName( request.getItemName() );
        bookDto.setPrice( request.getPrice() );
        bookDto.setStockQuantity( request.getStockQuantity() );

        return bookDto;
    }
}
