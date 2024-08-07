package com.sjw.doran.memberservice.service;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import com.sjw.doran.memberservice.vo.response.item.ItemSimpleWithCountResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BasketItemService {

    @Transactional(readOnly = true)
    List<ItemSimpleWithCountResponse> findAllByBasket(Basket basket) throws InterruptedException;

    @Transactional
    void addBasketItem(Basket basket, String userUuid, BasketItemCreateRequest basketItemCreateRequest) throws InterruptedException;

    @Transactional
    void deleteBasketItem(Basket basket, String userUuid, String itemUuid);
}
