package com.sjw.doran.itemservice.redis;

import com.sjw.doran.itemservice.mapper.CustomObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 전체 수정 예정
 */
@Component
@RequiredArgsConstructor
public class OrderedItemListCacheImpl implements OrderedItemListCache {

    private static final String KEY_PREFIX = "ordered_item_list:v1:";
    private static final Long EXPIRE_SECONDS = 60 * 60 * 24L; // a day
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    private final RedisTemplate<String, String> redisTemplate;

    private String generateCacheKey(Long itemId) {
        return KEY_PREFIX + itemId;
    }
}
