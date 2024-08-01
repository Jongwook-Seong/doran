package com.sjw.doran.memberservice.redis.repository;

import com.sjw.doran.memberservice.redis.data.CachedBasket;
import org.springframework.data.repository.CrudRepository;

public interface CachedBasketRedisRepository extends CrudRepository<CachedBasket, String> {
}
