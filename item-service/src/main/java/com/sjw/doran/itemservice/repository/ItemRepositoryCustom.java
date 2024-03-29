package com.sjw.doran.itemservice.repository;

import com.sjw.doran.itemservice.entity.Category;
import com.sjw.doran.itemservice.entity.Item;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ItemRepositoryCustom {

    @Transactional(readOnly = true)
    Optional<Item> findByItemUuid(String itemUuid);

    @Transactional(readOnly = true)
    List<Item> findByCategory(Category category);
}
