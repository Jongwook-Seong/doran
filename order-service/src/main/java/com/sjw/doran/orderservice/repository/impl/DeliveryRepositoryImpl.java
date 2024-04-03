package com.sjw.doran.orderservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.orderservice.entity.Delivery;
import com.sjw.doran.orderservice.entity.Order;
import com.sjw.doran.orderservice.entity.QDelivery;
import com.sjw.doran.orderservice.repository.DeliveryRepositoryCustom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import static com.sjw.doran.orderservice.entity.QDelivery.delivery;

public class DeliveryRepositoryImpl implements DeliveryRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public DeliveryRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Delivery findByOrder(Order order) {
        Delivery deliveryEntity = queryFactory
                .selectFrom(delivery)
                .where(delivery.order.eq(order))
                .fetchOne();
        return deliveryEntity;
    }
}
