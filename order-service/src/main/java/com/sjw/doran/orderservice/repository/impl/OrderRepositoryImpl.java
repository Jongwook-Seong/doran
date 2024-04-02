package com.sjw.doran.orderservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.orderservice.entity.Order;
import com.sjw.doran.orderservice.repository.OrderRepositoryCustom;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.sjw.doran.orderservice.entity.QOrder.order;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Order> findByOrderUuid(String orderUuid) {
        Order orderEntity = queryFactory
                .selectFrom(order)
                .where(order.orderUuid.eq(orderUuid))
                .fetchOne();
        return Optional.ofNullable(orderEntity);
    }
}
