package com.sjw.doran.itemservice.redis.service;

import com.sjw.doran.itemservice.redis.data.BestItem;

import java.util.List;

public interface BestItemCacheService {

    List<BestItem> getBestItems();
    void setBestItems();
    void setOrderedItemSales();
}
