package com.sjw.doran.orderservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.orderservice.repository.OrderItemRepositoryCustom;
import jakarta.persistence.EntityManager;

public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public OrderItemRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}
