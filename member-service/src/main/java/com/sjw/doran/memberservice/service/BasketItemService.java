package com.sjw.doran.memberservice.service;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import com.sjw.doran.memberservice.vo.response.ItemSimpleResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BasketItemService {

    @Transactional(readOnly = true)
    List<ItemSimpleResponse> findAllByBasket(Basket basket);

    @Transactional
    void addBasketItem(Basket basket, BasketItemCreateRequest basketItemCreateRequest);

    @Transactional
    void deleteBasketItem(Basket basket, String itemUuid);
}
