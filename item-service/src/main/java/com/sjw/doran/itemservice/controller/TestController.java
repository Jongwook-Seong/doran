package com.sjw.doran.itemservice.controller;

import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.entity.Category;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final ItemRepository itemRepository;

    @GetMapping("/test")
    public void test() {
        Item item1 = createBook("book1", 1000, 100, "url1");
        Item item2 = createBook("book2", 2000, 200, "url2");

        itemRepository.save(item1);
        itemRepository.save(item2);
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
