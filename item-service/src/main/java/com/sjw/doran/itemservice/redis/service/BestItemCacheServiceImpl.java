package com.sjw.doran.itemservice.redis.service;

import com.sjw.doran.itemservice.redis.data.BestItem;
import com.sjw.doran.itemservice.redis.data.OrderedItemSales;
import com.sjw.doran.itemservice.redis.repository.BestItemRedisRepository;
import com.sjw.doran.itemservice.redis.repository.OrderDataListRedisRepository;
import com.sjw.doran.itemservice.redis.repository.OrderedItemSalesRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BestItemCacheServiceImpl implements BestItemCacheService {

    private final OrderDataListRedisRepository orderDataListRedisRepository;
    private final OrderedItemSalesRedisRepository orderedItemSalesRedisRepository;
    private final BestItemRedisRepository bestItemRedisRepository;

    @Override
    public List<BestItem> getBestItems() {
        Iterable<BestItem> findBestItemIterable = bestItemRedisRepository.findAll();
        List<BestItem> bestItems = new ArrayList<>();
        findBestItemIterable.forEach(item -> bestItems.add(item));
        return bestItems;
    }

    @Override
    public void setBestItems() {
        Iterable<OrderedItemSales> allItemSales = orderedItemSalesRedisRepository.findAll();
        for (OrderedItemSales itemSales : allItemSales) {

        }
        bestItemRedisRepository.deleteAll();
    }

    @Override
    public void appendOrderData(String itemUuid, int orderQuantity) {

    }
}
