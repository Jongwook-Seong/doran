package com.sjw.doran.requestdatagenerator.item.service;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sjw.doran.requestdatagenerator.common.CustomObjectMapper;
import com.sjw.doran.requestdatagenerator.common.Generator;
import com.sjw.doran.requestdatagenerator.item.repository.ItemRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemRequestGenerateService {

    private final ItemRepository itemRepository;
    private final Generator generator;
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookCreateRequest {
        private String itemName;
        private int price;
        private int stockQuantity;
        private String author;
        private String isbn;
        private int pages;
        private Date publicationDate;
        private String contentsTable;
        private String bookReview;

//        public static BookCreateRequest getInstance(BookItem object) {
//            return BookCreateRequest.builder()
//                    .itemName(object.itemName)
//                    .price(object.price)
//                    .stockQuantity(object.stockQuantity)
//                    .author(object.author)
//                    .isbn(object.isbn)
//                    .pages(object.pages)
//                    .publicationDate(object.publicationDate)
//                    .contentsTable(object.contentsTable)
//                    .bookReview(object.bookReview)
//                    .build();
//        }
    }

//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class BookDto {
//        private String itemName;
//        private int price;
//        private int stockQuantity;
//        private String author;
//        private String isbn;
//        private int pages;
//        private Date publicationDate;
//        private String contentsTable;
//        private String bookReview;
//
//        public static BookDto getInstance(Book book) {
//            return BookDto.builder()
//                    .itemName(book.getItemName())
//                    .price(book.getPrice())
//                    .stockQuantity(book.getStockQuantity())
//                    .author(book.getAuthor())
//                    .isbn(book.getIsbn())
//                    .pages(book.getPages())
//                    .publicationDate(book.getPublicationDate())
//                    .contentsTable(book.getContentsTable())
//                    .bookReview(book.getBookReview())
//                    .build();
//        }
//    }

    public Map<Long, BookCreateRequest> createBookCreateRequests(long size) {
        Map<Long, BookCreateRequest> bookCreateRequestMap = new HashMap<>();
        for (long i = 1; i <= size; i++) {
            bookCreateRequestMap.put(i, generateBookCreateRequest());
        }
        return bookCreateRequestMap;
    }

    private BookCreateRequest generateBookCreateRequest() {
        String itemName = generator.generateRandomString(20);
        int price = generator.generateRandomInteger();
        int stockQuantity = 100000;
        String author = generator.generateRandomString(20);
        String isbn = generator.generateRandomString(20);
        int pages = generator.generateRandomInteger();
        Date publicationDate = new Date(
                generator.generateRandomInteger(2023),
                generator.generateRandomInteger(12),
                generator.generateRandomInteger(28));
        String contentsTable = generator.generateRandomString(20);
        String bookReview = generator.generateRandomString(20);
        return new BookCreateRequest(itemName, price, stockQuantity, author, isbn, pages, publicationDate, contentsTable, bookReview);
    }

    public void createBodyValueJsonFile() {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        List<String> allItemUuid = itemRepository.findAllItemUuid();
        for (String itemUuid : allItemUuid) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("itemUuid", itemUuid);
            arrayNode.add(node);
        }
        try {
            objectMapper.writeValue(new File("itemUuids.json"), arrayNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
