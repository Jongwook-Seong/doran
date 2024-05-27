package com.sjw.doran.itemservice.mapper;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.entity.Category;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    Book toBook(BookDto bookDto);
    @Mapping(target = "itemUuid", source = "itemUuid", defaultExpression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "category", source = "category", defaultExpression = "java(Category.BOOK)")
    BookDto toBookDto(BookCreateRequest request, String itemUuid, Category category);
}
