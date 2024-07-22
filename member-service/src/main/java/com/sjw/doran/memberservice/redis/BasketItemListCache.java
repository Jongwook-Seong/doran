package com.sjw.doran.memberservice.redis;

public interface BasketItemListCache {

    void set(CachedBasket cachedBasket);
    CachedBasket get(Long basketId);
    void addBasketItem(Long basketId, CachedBasket.CachedBasketItem basketItem);
    void removeBasketItem(Long basketId, CachedBasket.CachedBasketItem basketItem);
    void delete(Long basketId);
}
