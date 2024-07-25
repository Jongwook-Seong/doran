package com.sjw.doran.memberservice.redis;

public interface BasketItemListCache {

    void set(CachedBasket cachedBasket);
    CachedBasket get(String userUuid);
    void addBasketItem(String userUuid, CachedBasket.CachedBasketItem basketItem);
    void removeBasketItem(String userUuid, String itemUuid);
    void delete(String userUuid);
}
