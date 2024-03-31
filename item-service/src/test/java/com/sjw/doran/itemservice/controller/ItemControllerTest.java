package com.sjw.doran.itemservice.controller;

import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.entity.Category;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import com.sjw.doran.itemservice.vo.response.ItemSimpleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@RequiredArgsConstructor
class ItemControllerTest {

    @Autowired
    private ItemController itemController;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    void 아이템_저장_및_검색조회() {

        BookCreateRequest bookCreateRequest = new BookCreateRequest(
                "Spring", 10000, 100,
                "kim", "1111", 100, new Date(), "index", "review"
        );
        itemController.registerBook(bookCreateRequest);

        ResponseEntity<List<ItemSimpleResponse>> searchBooks = itemController.bookSearch("Spring");
        System.out.println("searchBooks = " + searchBooks);
    }

    Book createBook(String itemName, int price, int quantity, String itemImageUrl) {
        Book book = new Book();
        book.setItemUuid(UUID.randomUUID().toString());
        book.setItemName(itemName);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        book.setItemImageUrl(itemImageUrl);
        book.setCategory(Category.BOOK);

        return book;
    }
}