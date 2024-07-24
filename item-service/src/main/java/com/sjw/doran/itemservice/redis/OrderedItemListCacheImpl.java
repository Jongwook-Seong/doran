package com.sjw.doran.itemservice.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sjw.doran.itemservice.mapper.CustomObjectMapper;
import com.sjw.doran.itemservice.redis.data.OrderedItemSales;
import org.springframework.data.redis.core.RedisTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderedItemListCacheImpl implements OrderedItemListCache {

    private static final String KEY_PREFIX = "ordered_item_list:v1:";
    private static final Long EXPIRE_SECONDS = 60 * 60 * 24L; // a day
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void set(OrderedItemSales orderedItemSales) {
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(orderedItemSales);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.opsForValue().set(
                this.generateCacheKey(orderedItemSales.getId()), jsonString, Duration.ofSeconds(EXPIRE_SECONDS));
    }

    @Override
    public OrderedItemSales get(Long itemId) {
        String jsonString = redisTemplate.opsForValue().get(this.generateCacheKey(itemId));
        if (jsonString == null) return null;
        try {
            return objectMapper.readValue(jsonString, OrderedItemSales.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void increaseSales(Long itemId, int orderQuantity) {
        Long sales = redisTemplate.opsForHash().increment(this.generateCacheKey(itemId), "sales", orderQuantity);
        redisTemplate.opsForZSet().add("best_items", this.generateCacheKey(itemId), sales);
        String objectString;
        try {
            // TODO
            objectString = objectMapper.writeValueAsString(new OrderDataList.OrderData(orderQuantity, LocalDateTime.now()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.opsForList().leftPush(this.generateCacheKey(itemId), objectString);
    }

    @Override
    public List<String> getBestSalesItem() {
        return null;
    }

    private String generateCacheKey(Long itemId) {
        return KEY_PREFIX + itemId;
    }
}
