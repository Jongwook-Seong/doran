package com.sjw.doran.itemservice.redis;

import com.sjw.doran.itemservice.redis.data.OrderedItemSales;

import java.util.List;

public interface OrderedItemListCache {

    void set(OrderedItemSales orderedItemSales);
    OrderedItemSales get(Long itemId);
    void increaseSales(Long itemId, int orderQuantity);
    List<String> getBestSalesItem();
}
