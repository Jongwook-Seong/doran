package com.sjw.doran.memberservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.ItemDetail;
import com.sjw.doran.memberservice.entity.QItemDetail;
import com.sjw.doran.memberservice.repository.ItemDetailRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sjw.doran.memberservice.entity.QItemDetail.itemDetail;

public class ItemDetailRepositoryImpl implements ItemDetailRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemDetailRepositoryImpl(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDetail> findAllByBasket(Basket basket) {

        return queryFactory
                .selectFrom(itemDetail)
                .where(itemDetail.basket.eq(basket))
                .fetch();
    }
}
