package com.sjw.doran.authservice.redis.service;

public interface KeyRedisService {

    void set(String key, Object o, int minutes);
    Object get(String key);
    boolean delete(String key);
    boolean hasKey(String key);
    void setBlackList(String key, Object o, int minutes);
    Object getBlackList(String key);
    boolean deleteBlackList(String key);
    boolean hasKeyBlackList(String key);
}
