package com.sjw.doran.itemservice.repository;

import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.entity.Category;
import com.sjw.doran.itemservice.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ItemRepositoryCustom {

    @Transactional(readOnly = true)
    Optional<Item> findByItemUuid(String itemUuid);

    @Transactional(readOnly = true)
    List<Item> findByItemUuidList(List<String> itemUuidList);

    @Transactional(readOnly = true)
    List<Book> findBookByKeyword(String keyword);

    @Transactional(readOnly = true)
    List<Item> findByCategory(Category category);

    @Transactional
    void deleteByItemUuid(String itemUuid);
}
