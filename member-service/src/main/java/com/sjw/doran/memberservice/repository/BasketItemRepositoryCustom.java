package com.sjw.doran.memberservice.repository;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;

import java.util.List;

public interface BasketItemRepositoryCustom {

    List<BasketItem> findAllByBasket(Basket basket);

    void deleteByBasketAndItemUuid(Basket basket, String itemUuid);
}
