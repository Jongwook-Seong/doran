package com.sjw.doran.orderservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.orderservice.repository.DeliveryTrackingRepositoryCustom;
import jakarta.persistence.EntityManager;

public class DeliveryTrackingRepositoryImpl implements DeliveryTrackingRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public DeliveryTrackingRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}
