package com.sjw.doran.memberservice.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sjw.doran.memberservice.mapper.CustomObjectMapper;
import com.sjw.doran.memberservice.redis.data.CachedBasket;
import com.sjw.doran.memberservice.redis.repository.CachedBasketRedisRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketItemListCacheServiceImpl implements BasketItemListCacheService {

    private final CachedBasketRedisRepository cachedBasketRedisRepository;
//    private static final String KEY_PREFIX = "basket_item_list:v1:";
//    private static final Long EXPIRE_SECONDS = 60 * 60 * 24 * 7L; // a week
//    private static final Long EXPIRE_SECONDS = 60 * 30L; // 30 minutes
//    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

//    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void set(CachedBasket cachedBasket) {
//        String jsonString;
//        try {
//            jsonString = objectMapper.writeValueAsString(cachedBasket);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        redisTemplate.opsForValue().set(
//                this.generateCacheKey(cachedBasket.getUserUuid()), jsonString, Duration.ofSeconds(EXPIRE_SECONDS));
        cachedBasketRedisRepository.save(cachedBasket);
    }

    @Override
    @Cacheable(value = "BasketItemList")
    public CachedBasket get(String userUuid) {
        return cachedBasketRedisRepository.findById(userUuid).orElse(null);
//        String jsonString = redisTemplate.opsForValue().get(this.generateCacheKey(userUuid));
//        if (jsonString == null) return null;
//        try {
//            return objectMapper.readValue(jsonString, CachedBasket.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
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
        cachedBasketRedisRepository.save(cachedBasket);
//        String jsonString;
//        try {
//            jsonString = objectMapper.writeValueAsString(cachedBasket);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        redisTemplate.opsForValue().set(
//                this.generateCacheKey(cachedBasket.getUserUuid()), jsonString, Duration.ofSeconds(EXPIRE_SECONDS));
    }

    @Override
    public void removeBasketItem(String userUuid, String itemUuid) {
        CachedBasket cachedBasket = this.get(userUuid);
        if (cachedBasket != null) {
            cachedBasket.getItems().removeIf(item -> item.getItemUuid().equals(itemUuid));
            cachedBasketRedisRepository.save(cachedBasket);
//            String jsonString;
//            try {
//                jsonString = objectMapper.writeValueAsString(cachedBasket);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//            redisTemplate.opsForValue().set(
//                    this.generateCacheKey(cachedBasket.getUserUuid()), jsonString, Duration.ofSeconds(EXPIRE_SECONDS));
        }
    }

    @Override
    public void delete(String userUuid) {
        cachedBasketRedisRepository.deleteById(userUuid);
//        redisTemplate.delete(this.generateCacheKey(userUuid));
    }

//    private String generateCacheKey(String userUuid) {
//        return KEY_PREFIX + userUuid;
//    }
}
