package com.sjw.doran.itemservice.repository;

import com.sjw.doran.itemservice.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
}
