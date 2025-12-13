package com.sjw.doran.authsimpleservice.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    // Refresh Token 저장 시 만료 시간도 함께 설정
    @Override
    public void saveRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue().set(
                username,
                refreshToken,
                7, // 만료 기간 7일
                TimeUnit.DAYS
        );
    }

    @Override
    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(username);
    }

    @Override
    public void deleteRefreshToken(String username) {
        redisTemplate.delete(username);
    }
}
