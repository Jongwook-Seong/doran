package com.sjw.doran.itemservice.mapper;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.entity.Category;
import com.sjw.doran.itemservice.kafka.common.OperationType;
import com.sjw.doran.itemservice.kafka.item.ItemTopicMessage;
import com.sjw.doran.itemservice.mongodb.item.ItemDocument;
import com.sjw.doran.itemservice.redis.data.BestItem;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import com.sjw.doran.itemservice.vo.response.ItemSimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    Book toBook(BookDto bookDto);
    BookDto toBookDto(Book book);
    @Mapping(target = "itemUuid", source = "itemUuid", defaultExpression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "category", source = "category", defaultExpression = "java(Category.BOOK)")
    BookDto toBookDto(BookCreateRequest request, String itemUuid, Category category);
    ItemSimpleResponse toItemSimpleResponse(BookDto bookDto);

    @Mapping(target = "id", source = "book.id")
    @Mapping(target = "operationType", source = "operationType")
    @Mapping(target = "payload", expression = "java(mapPayload(book, orderQuantity))")
    ItemTopicMessage toItemTopicMessage(Book book, int orderQuantity, OperationType operationType);

    @Mapping(target = "author", source = "book.author")
    @Mapping(target = "isbn", source = "book.isbn")
    @Mapping(target = "pages", source = "book.pages")
    @Mapping(target = "publicationDate", source = "book.publicationDate")
    @Mapping(target = "contentsTable", source = "book.contentsTable")
    @Mapping(target = "bookReview", source = "book.bookReview")
    ItemTopicMessage.BookData toItemTopicMessageBookData(Book book);

    @Mapping(target = "id", source = "book.id")
    @Mapping(target = "itemUuid", source = "book.itemUuid")
    @Mapping(target = "itemName", source = "book.itemName")
    @Mapping(target = "price", source = "book.price")
    @Mapping(target = "stockQuantity", source = "book.stockQuantity")
    @Mapping(target = "itemImageUrl", source = "book.itemImageUrl")
    @Mapping(target = "category", source = "book.category")
    @Mapping(target = "bookData", source = "book")
    @Mapping(target = "createdAt", source = "book.createdAt")
    @Mapping(target = "modifiedAt", source = "book.modifiedAt")
    @Mapping(target = "latestOrderQuantity", source = "orderQuantity")
    ItemTopicMessage.Payload mapPayload(Book book, int orderQuantity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "itemUuid", source = "itemUuid")
    @Mapping(target = "itemName", source = "itemName")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "stockQuantity", source = "stockQuantity")
    @Mapping(target = "itemImageUrl", source = "itemImageUrl")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "bookInfo.author", source = "bookData.author")
    @Mapping(target = "bookInfo.isbn", source = "bookData.isbn")
    @Mapping(target = "bookInfo.pages", source = "bookData.pages")
    @Mapping(target = "bookInfo.publicationDate", source = "bookData.publicationDate")
    @Mapping(target = "bookInfo.contentsTable", source = "bookData.contentsTable")
    @Mapping(target = "bookInfo.bookReview", source = "bookData.bookReview")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "modifiedAt", source = "modifiedAt")
    ItemDocument toItemDocument(ItemTopicMessage.Payload payload);

    @Mapping(target = "id", source = "itemDocument.id")
    @Mapping(target = "itemUuid", source = "itemDocument.itemUuid")
    @Mapping(target = "itemName", source = "itemDocument.itemName")
    @Mapping(target = "price", source = "itemDocument.price")
    @Mapping(target = "stockQuantity", source = "itemDocument.stockQuantity")
    @Mapping(target = "itemImageUrl", source = "itemDocument.itemImageUrl")
    @Mapping(target = "category", source = "itemDocument.category")
    @Mapping(target = "author", source = "itemDocument.bookInfo.author")
    @Mapping(target = "isbn", source = "itemDocument.bookInfo.isbn")
    @Mapping(target = "pages", source = "itemDocument.bookInfo.pages")
    @Mapping(target = "publicationDate", source = "itemDocument.bookInfo.publicationDate")
    @Mapping(target = "contentsTable", source = "itemDocument.bookInfo.contentsTable")
    @Mapping(target = "bookReview", source = "itemDocument.bookInfo.bookReview")
    Book toBook(ItemDocument itemDocument);

    BestItem.BookInfo toBestItemBookInfo(ItemDocument.BookInfo bookInfo);
}
