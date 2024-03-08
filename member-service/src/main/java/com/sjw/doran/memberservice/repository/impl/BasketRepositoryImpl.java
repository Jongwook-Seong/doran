package com.sjw.doran.memberservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.entity.QBasket;
import com.sjw.doran.memberservice.repository.BasketRepositoryCustom;
import jakarta.persistence.EntityManager;

import static com.sjw.doran.memberservice.entity.QBasket.basket;

public class BasketRepositoryImpl implements BasketRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public BasketRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Basket findByMember(Member member) {

        return queryFactory
                .selectFrom(basket)
                .where(basket.member.eq(member))
                .fetchOne();
    }
}
