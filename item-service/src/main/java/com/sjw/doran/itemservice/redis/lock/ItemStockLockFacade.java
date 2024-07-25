package com.sjw.doran.itemservice.redis.lock;

import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemStockLockFacade {

    private final RedissonClient redissonClient;
    private final ItemRepository itemRepository;

    private static final String LOCK_KEY_PREFIX = "itemStockLock:";

    public List<Item> decreaseStock(List<String> itemUuidList, List<Integer> countList) {

        List<Item> items = itemRepository.findByItemUuidList(itemUuidList);
        Map<String, Item> itemMap = items.stream()
                .collect(Collectors.toMap(Item::getItemUuid, item -> item));

        for (int i = 0; i < itemUuidList.size(); i++) {
            String itemUuid = itemUuidList.get(i);
            int count = countList.get(i);

            String lockKey = LOCK_KEY_PREFIX + itemUuidList.get(i);
            RLock lock = redissonClient.getLock(lockKey);

            try {
                boolean available = lock.tryLock(15, 1, TimeUnit.SECONDS);
                if (available) {
                    try {
                        Item item = itemMap.get(itemUuid);
                        if (item == null)
                            throw new IllegalArgumentException("Invalid itemUuid: " + itemUuid);
                        if (item.getStockQuantity() < count)
                            throw new IllegalArgumentException("Not enough stock for itemUuid: " + itemUuid);
                        item.removeStock(count);
                    } finally {
                        lock.unlock();
                    }
                } else {
                    throw new IllegalStateException("Unable to acquire lock for itemUuid: " + itemUuid);
                }
            } catch (InterruptedException e) {
                throw new IllegalStateException("Thread was interrupted while trying to acquire lock", e);
            }
        }
        List<Item> updatedItems = itemRepository.saveAll(items);
        return updatedItems.stream().map(itemMap::get).collect(Collectors.toList());
    }
}
