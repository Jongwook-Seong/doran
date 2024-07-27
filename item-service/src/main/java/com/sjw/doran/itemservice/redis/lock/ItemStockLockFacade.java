package com.sjw.doran.itemservice.redis.lock;

import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemStockLockFacade {

    private final RedissonClient redissonClient;
    private final ItemService itemService;

    private static final String LOCK_KEY_PREFIX = "itemStockLock:";

    public List<Item> decreaseStock(List<String> itemUuidList, List<Integer> countList) {

        String lockKey  = LOCK_KEY_PREFIX + "decreaseStock";
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean available = lock.tryLock(15, 1, TimeUnit.SECONDS);
            if (!available) {
                log.info("Failed to acquire lock {}", lockKey);
                return null;
            }
            return itemService.subtractItems(itemUuidList, countList);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Thread was interrupted while trying to acquire lock", e);
        } finally {
            lock.unlock();
        }
    }

    public List<Item> increaseStock(List<String> itemUuidList, List<Integer> countList) {

        String lockKey  = LOCK_KEY_PREFIX + "increaseStock";
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean available = lock.tryLock(15, 1, TimeUnit.SECONDS);
            if (!available) {
                log.info("Failed to acquire lock {}", lockKey);
                return null;
            }
            return itemService.restoreItems(itemUuidList, countList);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Thread was interrupted while trying to acquire lock", e);
        } finally {
            lock.unlock();
        }
    }
}
