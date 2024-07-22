package com.sjw.doran.memberservice.service;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import org.springframework.transaction.annotation.Transactional;

public interface BasketService {

    @Transactional
    void setBasket(Member member);

    @Transactional(readOnly = true)
    Basket findBasket(Member member);

    @Transactional
    void deleteBasket(Member member);

    @Transactional
    void addBasketItem(String userUuid, BasketItemCreateRequest basketItemCreateRequest) throws InterruptedException;

    @Transactional
    void deleteBasketItem(String userUuid, String itemUuid);
}
