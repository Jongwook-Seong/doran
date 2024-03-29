package com.sjw.doran.itemservice.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.itemservice.entity.Category;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.entity.QItem;
import com.sjw.doran.itemservice.repository.ItemRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.sjw.doran.itemservice.entity.QItem.item;

public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Item> findByItemUuid(String itemUuid) {

        Item findItem = queryFactory
                .selectFrom(QItem.item)
                .where(QItem.item.itemUuid.eq(itemUuid))
                .fetchOne();

        return Optional.of(findItem);
    }

    @Override
    public List<Item> findByCategory(Category category) {

        List<Item> findItems = queryFactory
                .selectFrom(item)
                .where(item.categories.contains(category))
                .fetch();

        return findItems;
    }

//    private BooleanBuilder matchCategory(Category category) {
//
//    }
}
