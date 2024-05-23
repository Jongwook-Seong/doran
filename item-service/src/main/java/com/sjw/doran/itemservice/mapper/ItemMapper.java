package com.sjw.doran.itemservice.util;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    BookDto toBookDto(BookCreateRequest request);
    Book toBook(BookDto bookDto);
}
