package com.sjw.doran.itemservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.itemservice.entity.*;
import com.sjw.doran.itemservice.repository.ItemRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.sjw.doran.itemservice.entity.QBook.book;
import static com.sjw.doran.itemservice.entity.QItem.item;

public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Item> findByItemUuid(String itemUuid) {

        System.out.println("itemUuid = " + itemUuid);
        Item findItem = queryFactory
                .selectFrom(QItem.item)
                .where(QItem.item.itemUuid.eq(itemUuid))
                .fetchOne();

        return Optional.of(findItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findBookByKeyword(String keyword) {

        List<Book> findBooks = queryFactory.selectFrom(book)
                .where(book.itemName.contains(keyword))
                .distinct()
                .fetch();

        return findBooks;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> findByCategory(Category category) {

        List<Item> findItems = queryFactory
                .selectFrom(item)
                .where(item.category.eq(category))
                .fetch();

        return findItems;
    }

    @Override
    public void deleteByItemUuid(String itemUuid) {

        queryFactory
                .delete(item)
                .where(item.itemUuid.eq(itemUuid))
                .execute();
    }
}
