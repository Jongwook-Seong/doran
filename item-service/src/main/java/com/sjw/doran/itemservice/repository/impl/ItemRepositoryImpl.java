package com.sjw.doran.itemservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.itemservice.entity.*;
import com.sjw.doran.itemservice.repository.ItemRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Item findItem = queryFactory
                .selectFrom(QItem.item)
                .where(QItem.item.itemUuid.eq(itemUuid))
                .fetchOne();

        return Optional.of(findItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> findByItemUuidList(List<String> itemUuidList) {
        List<Item> itemList = queryFactory
                .selectFrom(item)
                .where(item.itemUuid.in(itemUuidList))
                .fetch();

        Map<String, Item> itemMap = itemList.stream().collect(Collectors.toMap(Item::getItemUuid, i -> i));
        List<Item> orderedItemList = itemUuidList.stream().map(itemMap::get).collect(Collectors.toList());
        return orderedItemList;
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

    @Override
    @Transactional
    public void updateStockQuantity(List<String> itemUuidList, List<Integer> countList) {

        Map<String, Integer> itemUuidCountMap = new HashMap<>();
        for (int i = 0; i < itemUuidList.size(); i++) {
            String itemUuid = itemUuidList.get(i);
            Integer count = countList.get(i);
            itemUuidCountMap.put(itemUuid, count);
        }

        List<Item> items = queryFactory
                .selectFrom(item)
                .where(item.itemUuid.in(itemUuidList))
                .fetch();

        for (Item item : items) {
            Integer count = itemUuidCountMap.get(item.getItemUuid());
            if (count != null) {
                item.setStockQuantity(item.getStockQuantity() - count);
            }
        }
    }
}
