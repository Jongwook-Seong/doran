package com.sjw.doran.memberservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.repository.BasketItemRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sjw.doran.memberservice.entity.QBasketItem.basketItem;

public class BasketItemRepositoryImpl implements BasketItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public BasketItemRepositoryImpl(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BasketItem> findAllByBasket(Basket basket) {

        return queryFactory
                .selectFrom(basketItem)
                .where(basketItem.basket.eq(basket))
                .fetch();
    }

    @Override
    @Transactional
    public void deleteByBasketAndItemUuid(Basket basket, String itemUuid) {
        queryFactory
                .delete(basketItem)
                .where(
                        basketItem.basket.eq(basket),
                        basketItem.itemUuid.eq(itemUuid))
                .execute();
    }
}
