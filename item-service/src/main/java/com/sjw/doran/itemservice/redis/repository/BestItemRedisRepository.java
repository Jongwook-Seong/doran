package com.sjw.doran.itemservice.redis.repository;

import com.sjw.doran.itemservice.redis.data.BestItem;
import org.springframework.data.repository.CrudRepository;

public interface BestItemRedisRepository extends CrudRepository<BestItem, String> {
}
