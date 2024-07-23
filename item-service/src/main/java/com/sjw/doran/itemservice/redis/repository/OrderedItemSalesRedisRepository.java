package com.sjw.doran.itemservice.redis.repository;

import com.sjw.doran.itemservice.redis.data.OrderedItemSales;
import org.springframework.data.repository.CrudRepository;

public interface OrderedItemSalesRedisRepository extends CrudRepository<OrderedItemSales, String> {
}
