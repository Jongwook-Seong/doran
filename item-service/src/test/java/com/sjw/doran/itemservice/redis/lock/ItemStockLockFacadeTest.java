package com.sjw.doran.itemservice.redis.lock;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.mapper.BookMapper;
import com.sjw.doran.itemservice.repository.ItemRepository;
import com.sjw.doran.itemservice.service.ItemService;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ItemStockLockFacadeTest {

    @Autowired
    private ItemStockLockFacade itemStockLockFacade;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookMapper bookMapper;

    private String itemUuid;

    @BeforeEach
    void before() {
        BookCreateRequest request = new BookCreateRequest();
        request.setStockQuantity(100);
        BookDto bookDto = bookMapper.toBookDto(request, null, null);
        Item item = bookMapper.toBook(bookDto);
        this.itemUuid = item.getItemUuid();
        itemRepository.saveAndFlush(item);
    }

    @AfterEach
    void after() {
        itemRepository.deleteAll();
    }

    @Test
    void decreaseStock_동시_100개_주문() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    Item item = itemStockLockFacade.decreaseStock(List.of(itemUuid), List.of(1)).get(0);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Item item = itemRepository.findByItemUuid(itemUuid).orElseThrow();
        assertEquals(0, item.getStockQuantity());
    }
}