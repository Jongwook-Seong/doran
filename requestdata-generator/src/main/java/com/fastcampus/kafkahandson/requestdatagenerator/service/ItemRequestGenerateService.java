package com.fastcampus.kafkahandson.requestdatagenerator.service;

import com.fastcampus.kafkahandson.requestdatagenerator.common.Generator;
import com.fastcampus.kafkahandson.requestdatagenerator.common.Memory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemRequestGenerateService {

    private final Memory memory = Memory.getInstance();
    private final Generator generator;

    public ItemRequestGenerateService(Generator generator) {
        this.generator = generator;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookItem {
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

        public static BookCreateRequest getInstance(BookItem object) {
            return BookCreateRequest.builder()
                    .itemName(object.itemName)
                    .price(object.price)
                    .stockQuantity(object.stockQuantity)
                    .author(object.author)
                    .isbn(object.isbn)
                    .pages(object.pages)
                    .publicationDate(object.publicationDate)
                    .contentsTable(object.contentsTable)
                    .bookReview(object.bookReview)
                    .build();
        }
    }

    public void setBookItems(long size) {
        for (long i = 1; i <= size; i++) {
            BookItem bookItem = generate();
            memory.getItemDataMap().put(i, bookItem);
        }
    }

    private BookItem generate() {
        String itemName = generator.generateRandomString(20);
        int price = generator.generateRandomInteger();
        int stockQuantity = generator.generateRandomInteger();
        String author = generator.generateRandomString(20);
        String isbn = generator.generateRandomString(20);
        int pages = generator.generateRandomInteger();
        Date publicationDate = new Date(
                generator.generateRandomInteger(2023),
                generator.generateRandomInteger(12),
                generator.generateRandomInteger(28));
        String contentsTable = generator.generateRandomString(20);
        String bookReview = generator.generateRandomString(20);
        return new BookItem(itemName, price, stockQuantity, author, isbn, pages, publicationDate, contentsTable, bookReview);
    }
}
