package com.sjw.doran.authsimpleservice.redis.service;

public interface RedisService {

    void saveRefreshToken(String username, String refreshToken);
    String getRefreshToken(String username);
    void deleteRefreshToken(String username);
}
