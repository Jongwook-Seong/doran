package com.sjw.doran.itemservice.mapper;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    Book toBook(BookDto bookDto);
    BookDto toBookDto(BookCreateRequest request);
}
