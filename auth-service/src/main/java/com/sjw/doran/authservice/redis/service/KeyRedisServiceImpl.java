package com.sjw.doran.authservice.redis.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class KeyRedisServiceImpl implements KeyRedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, String> blackListRedisTemplate;

    public KeyRedisServiceImpl(@Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplate,
                               @Qualifier("blackListRedisTemplate") RedisTemplate<String, String> blackListRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.blackListRedisTemplate = blackListRedisTemplate;
    }

    @Override
    public void set(String key, Object o, int minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>((Class<Object>) o.getClass()));
        redisTemplate.opsForValue().set(key, (String) o, minutes, TimeUnit.MINUTES);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void setBlackList(String key, Object o, int minutes) {
        blackListRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>((Class<Object>) o.getClass()));
        blackListRedisTemplate.opsForValue().set(key, (String) o, minutes, TimeUnit.MINUTES);
    }

    @Override
    public Object getBlackList(String key) {
        return blackListRedisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean deleteBlackList(String key) {
        return Boolean.TRUE.equals(blackListRedisTemplate.delete(key));
    }

    @Override
    public boolean hasKeyBlackList(String key) {
        return Boolean.TRUE.equals(blackListRedisTemplate.hasKey(key));
    }
}
