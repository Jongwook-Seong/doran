package com.sjw.doran.itemservice.redis.repository;

import com.sjw.doran.itemservice.redis.data.OrderDataList;
import org.springframework.data.repository.CrudRepository;

public interface OrderDataListRedisRepository extends CrudRepository<OrderDataList, String> {
}
