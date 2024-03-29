package com.sjw.doran.memberservice.service;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BasketItemService {

    @Transactional(readOnly = true)
    List<BasketItem> findAllByBasket(Basket basket);

    @Transactional
    void addBasketItem(Basket basket, BasketItemCreateRequest basketItemCreateRequest);

    @Transactional
    void deleteBasketItem(Basket basket, String itemUuid);
}
