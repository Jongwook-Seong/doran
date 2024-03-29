package com.sjw.doran.itemservice.repository.impl;

import com.sjw.doran.itemservice.entity.Artwork;
import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.entity.Category;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.repository.ItemRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ItemRepositoryImplTest {

    @Autowired
    EntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findByItemUuid() {

        Item item = createBook("book1", 1000, 100, "url1");
        itemRepository.save(item);

        String itemUuid = item.getItemUuid();
        Item findItem = itemRepository.findByItemUuid(itemUuid).orElseThrow(RuntimeException::new);

        assertThat(findItem).isEqualTo(item);
    }

    @Test
    void findByCategory() {

        Item item1 = createBook("book1", 1000, 100, "url1");
        Item item2 = createBook("book2", 2000, 200, "url2");
        Item item3 = createArtwork("artwork1", 3000, 300, "url3");

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        Item findItem1 = itemRepository.findByItemUuid(item1.getItemUuid()).orElseThrow(RuntimeException::new);
        Item findItem2 = itemRepository.findByItemUuid(item2.getItemUuid()).orElseThrow(RuntimeException::new);
        Item findItem3 = itemRepository.findByItemUuid(item3.getItemUuid()).orElseThrow(RuntimeException::new);

        Category category = new Category();
        category.setName("Book");
        em.persist(category);
        System.out.println("category.getItems() = " + category.getItems()); //null
        System.out.println("category.getName() = " + category.getName());
        List<Item> findItems = itemRepository.findByCategory(category);

        System.out.println("findItems = " + findItems);
        assertThat(findItems).contains(findItem1);
        assertThat(findItems).contains(findItem2);
        assertThat(findItems).doesNotContain(findItem3);
    }

    Book createBook(String itemName, int price, int quantity, String itemImageUrl) {
        Book book = new Book();
        book.setItemUuid(UUID.randomUUID().toString());
        book.setItemName(itemName);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        book.setItemImageUrl(itemImageUrl);

        Category category = new Category();
        category.setName("Book");
        category.addChildCategory(category);
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(category);

        book.setCategories(categories);
        return book;
    }

    Artwork createArtwork(String itemName, int price, int quantity, String itemImageUrl) {
        Artwork artwork = new Artwork();
        artwork.setItemUuid(UUID.randomUUID().toString());
        artwork.setItemName(itemName);
        artwork.setPrice(price);
        artwork.setStockQuantity(quantity);
        artwork.setItemImageUrl(itemImageUrl);

        Category category = new Category();
        category.setName("Artwork");
        category.addChildCategory(category);
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(category);

        artwork.setCategories(categories);
        return artwork;
    }
}