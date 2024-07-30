package com.sjw.doran.requestdatagenerator.item.repository;

import com.sjw.doran.requestdatagenerator.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Transactional(value = "itemTransactionManager", readOnly = true)
    List<Item> findByItemUuidIn(@Param("itemUuidList") List<String> itemUuidList);
}
