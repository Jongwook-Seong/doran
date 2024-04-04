package com.sjw.doran.orderservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.orderservice.entity.Delivery;
import com.sjw.doran.orderservice.entity.DeliveryTracking;
import com.sjw.doran.orderservice.entity.QDeliveryTracking;
import com.sjw.doran.orderservice.repository.DeliveryTrackingRepositoryCustom;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.sjw.doran.orderservice.entity.QDeliveryTracking.deliveryTracking;

public class DeliveryTrackingRepositoryImpl implements DeliveryTrackingRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public DeliveryTrackingRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<DeliveryTracking> findAllByDelivery(Delivery delivery) {
        List<DeliveryTracking> deliveryTrackingList = queryFactory
                .selectFrom(deliveryTracking)
                .where(deliveryTracking.delivery.eq(delivery))
                .orderBy(deliveryTracking.postDateTime.desc())
                .fetch();
        return deliveryTrackingList;
    }
}
