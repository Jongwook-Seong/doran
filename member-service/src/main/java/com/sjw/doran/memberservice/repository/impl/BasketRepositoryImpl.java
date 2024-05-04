package com.sjw.doran.memberservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.repository.BasketRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import static com.sjw.doran.memberservice.entity.QBasket.basket;

public class BasketRepositoryImpl implements BasketRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public BasketRepositoryImpl(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional(readOnly = true)
    public Basket findByMember(Member member) {

//        return queryFactory
//                .selectFrom(basket)
//                .where(basket.member.eq(member))
//                .fetchOne();

        Basket basket1 = queryFactory
                .selectFrom(basket)
                .where(basket.member.eq(member))
                .fetchOne();

        System.out.println("basket1 = " + basket1);
        System.out.println("basket1.getMember() = " + basket1.getMember());
        System.out.println("basket1.getBasketItems() = " + basket1.getBasketItems());

        return queryFactory
                .selectFrom(basket)
                .where(basket.member.eq(member))
                .fetchOne();
    }

    @Override
    @Transactional
    public void deleteByMember(Member member) {
        queryFactory
                .delete(basket)
                .where(basket.member.eq(member))
                .execute();
    }
}
