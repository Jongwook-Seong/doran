package com.sjw.doran.itemservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.itemservice.repository.ItemRepositoryCustom;
import jakarta.persistence.EntityManager;

public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}
