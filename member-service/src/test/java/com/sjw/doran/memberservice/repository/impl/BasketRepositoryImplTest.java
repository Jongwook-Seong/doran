package com.sjw.doran.memberservice.repository.impl;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.repository.BasketRepository;
import com.sjw.doran.memberservice.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@Transactional
class BasketRepositoryImplTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BasketRepository basketRepository;

    @Test
    void findByMember() {

        Member member = new Member("111", "member1", "url1");
        memberRepository.save(member);

        Basket findBasket = basketRepository.findByMember(member);
        Assertions.assertThat(findBasket).isEqualTo(member.getBasket());
    }
}