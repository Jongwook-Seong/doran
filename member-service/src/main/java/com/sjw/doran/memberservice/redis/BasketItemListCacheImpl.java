package com.sjw.doran.memberservice.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sjw.doran.memberservice.mapper.CustomObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BasketItemListCacheImpl implements BasketItemListCache {

    private static final String KEY_PREFIX = "basket_item_list:v1:";
    private static final Long EXPIRE_SECONDS = 60 * 60 * 24 * 7L; // a week
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void set(CachedBasket cachedBasket) {
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(cachedBasket);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.opsForValue().set(
                this.generateCacheKey(cachedBasket.getUserUuid()), jsonString, Duration.ofSeconds(EXPIRE_SECONDS));
    }

    @Override
    public CachedBasket get(String userUuid) {
        String jsonString = redisTemplate.opsForValue().get(this.generateCacheKey(userUuid));
        if (jsonString == null) return null;
        try {
            return objectMapper.readValue(jsonString, CachedBasket.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addBasketItem(String userUuid, CachedBasket.CachedBasketItem basketItem) {
        CachedBasket cachedBasket = this.get(userUuid);
        if (cachedBasket == null) {
            cachedBasket = new CachedBasket(userUuid, List.of(basketItem));
        } else {
            if (cachedBasket.getItems() == null)
                cachedBasket.setItems(List.of(basketItem));
            else
                cachedBasket.getItems().add(basketItem);
        }
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(cachedBasket);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.opsForValue().set(
                this.generateCacheKey(cachedBasket.getUserUuid()), jsonString, Duration.ofSeconds(EXPIRE_SECONDS));
//        }
    }

    @Override
    public void removeBasketItem(String userUuid, String itemUuid) {
        CachedBasket cachedBasket = this.get(userUuid);
        if (cachedBasket != null) {
            cachedBasket.getItems().removeIf(item -> item.getItemUuid().equals(itemUuid));
            String jsonString;
            try {
                jsonString = objectMapper.writeValueAsString(cachedBasket);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            redisTemplate.opsForValue().set(
                    this.generateCacheKey(cachedBasket.getUserUuid()), jsonString, Duration.ofSeconds(EXPIRE_SECONDS));
        }
    }

    @Override
    public void delete(String userUuid) {
        redisTemplate.delete(this.generateCacheKey(userUuid));
    }

    private String generateCacheKey(String userUuid) {
        return KEY_PREFIX + userUuid;
    }
}
