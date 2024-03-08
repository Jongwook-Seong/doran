package com.sjw.doran.memberservice.repository.impl;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.repository.BasketRepository;
import com.sjw.doran.memberservice.repository.BasketBasketItemRepository;
import com.sjw.doran.memberservice.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class BasketItemRepositoryImplTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    BasketBasketItemRepository basketItemRepository;

    @Test
    void findAllByBasket() {

        Member member = new Member("111", "member1", "url1");
        memberRepository.save(member);

        Basket basket = new Basket(member);
        basketRepository.save(basket);
        Basket findBasket = basketRepository.findByMember(member);

        BasketItem basketItem1 = new BasketItem(findBasket, "123", 2);
        BasketItem basketItem2 = new BasketItem(findBasket, "456", 4);
        basketItemRepository.save(basketItem1);
        basketItemRepository.save(basketItem2);

        List<BasketItem> findBasketItems = basketItemRepository.findAllByBasket(findBasket);

        log.info("itemDetail1 = " + basketItem1);
        log.info("itemDetail2 = " + basketItem2);
        log.info("findItemDetails = " + findBasketItems);

        assertThat(findBasketItems).contains(basketItem1);
        assertThat(findBasketItems).contains(basketItem2);
    }
}