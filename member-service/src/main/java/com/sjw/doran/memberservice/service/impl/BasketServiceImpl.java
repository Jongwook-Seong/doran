package com.sjw.doran.memberservice.service.impl;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.repository.BasketRepository;
import com.sjw.doran.memberservice.repository.MemberRepository;
import com.sjw.doran.memberservice.service.BasketItemService;
import com.sjw.doran.memberservice.service.BasketService;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {

    private final MemberRepository memberRepository;
    private final BasketRepository basketRepository;
    private final BasketItemService basketItemService;

    @Override
    @Transactional
    public void setBasket(Member member) {
        basketRepository.save(new Basket(member));
    }

    @Override
    @Transactional(readOnly = true)
    public Basket findBasket(Member member) {
        return basketRepository.findByMember(member);
    }

    @Override
    @Transactional
    public void addBasketItem(String userUuid, BasketItemCreateRequest basketItemCreateRequest) {
        Optional<Member> member = memberRepository.findByUserUuid(userUuid);
        if (member.isEmpty()) {
            return;
        }
        Basket basket = basketRepository.findByMember(member.get());
        basketItemService.addBasketItem(basket, basketItemCreateRequest);
    }
}
