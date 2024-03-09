package com.sjw.doran.memberservice.service;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.Member;
import org.springframework.transaction.annotation.Transactional;

public interface BasketService {

    @Transactional
    void setBasket(Member member);

    @Transactional(readOnly = true)
    Basket findBasket(Member member);
}
