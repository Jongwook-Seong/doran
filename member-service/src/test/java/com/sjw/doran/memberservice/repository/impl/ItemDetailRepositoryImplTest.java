package com.sjw.doran.memberservice.repository.impl;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.ItemDetail;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.repository.BasketRepository;
import com.sjw.doran.memberservice.repository.ItemDetailRepository;
import com.sjw.doran.memberservice.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class ItemDetailRepositoryImplTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    ItemDetailRepository itemDetailRepository;

    @Test
    void findAllByBasket() {

        Member member = new Member("111", "member1", "url1");
        memberRepository.save(member);

        Basket basket = new Basket(member);
        basketRepository.save(basket);
        Basket findBasket = basketRepository.findByMember(member);

        ItemDetail itemDetail1 = new ItemDetail(findBasket, "123", 2);
        ItemDetail itemDetail2 = new ItemDetail(findBasket, "456", 4);
        itemDetailRepository.save(itemDetail1);
        itemDetailRepository.save(itemDetail2);

        List<ItemDetail> findItemDetails = itemDetailRepository.findAllByBasket(findBasket);

        log.info("itemDetail1 = " + itemDetail1);
        log.info("itemDetail2 = " + itemDetail2);
        log.info("findItemDetails = " + findItemDetails);

        assertThat(findItemDetails).contains(itemDetail1);
        assertThat(findItemDetails).contains(itemDetail2);
    }
}