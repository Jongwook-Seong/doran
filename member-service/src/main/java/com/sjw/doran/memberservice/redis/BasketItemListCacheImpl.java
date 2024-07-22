package com.sjw.doran.memberservice.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sjw.doran.memberservice.mapper.CustomObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

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
                this.generateCacheKey(cachedBasket.getId()), jsonString, Duration.ofSeconds(EXPIRE_SECONDS));
    }

    @Override
    public CachedBasket get(Long basketId) {
        String jsonString = redisTemplate.opsForValue().get(this.generateCacheKey(basketId));
        if (jsonString == null) return null;
        try {
            return objectMapper.readValue(jsonString, CachedBasket.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addBasketItem(Long basketId, CachedBasket.CachedBasketItem basketItem) {
        CachedBasket cachedBasket = this.get(basketId);
        if (cachedBasket != null) {
            cachedBasket.getItems().add(basketItem);
            String jsonString;
            try {
                jsonString = objectMapper.writeValueAsString(cachedBasket);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            redisTemplate.opsForValue().set(
                    this.generateCacheKey(cachedBasket.getId()), jsonString, Duration.ofSeconds(EXPIRE_SECONDS));
        }
    }

    @Override
    public void removeBasketItem(Long basketId, CachedBasket.CachedBasketItem basketItem) {
        CachedBasket cachedBasket = this.get(basketId);
        if (cachedBasket != null) {
            cachedBasket.getItems().removeIf(item ->
                    item.getItemUuid().equals(basketItem.getItemUuid()));
            String jsonString;
            try {
                jsonString = objectMapper.writeValueAsString(cachedBasket);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            redisTemplate.opsForValue().set(
                    this.generateCacheKey(cachedBasket.getId()), jsonString, Duration.ofSeconds(EXPIRE_SECONDS));
        }
    }

    @Override
    public void delete(Long basketId) {
        redisTemplate.delete(this.generateCacheKey(basketId));
    }

    private String generateCacheKey(Long basketId) {
        return KEY_PREFIX + basketId;
    }
}
