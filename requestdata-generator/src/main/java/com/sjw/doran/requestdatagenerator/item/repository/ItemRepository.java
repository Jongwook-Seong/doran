package com.sjw.doran.requestdatagenerator.item.repository;

import com.sjw.doran.requestdatagenerator.item.entity.Book;
import com.sjw.doran.requestdatagenerator.item.entity.Category;
import com.sjw.doran.requestdatagenerator.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Transactional(value = "itemTransactionManager", readOnly = true)
    List<Item> findByItemUuidIn(@Param("itemUuidList") List<String> itemUuidList);


    @Transactional(value = "itemTransactionManager", readOnly = true)
    @Query(value = "SELECT * FROM Item i WHERE i.category = 'BOOK' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Book> findAnyByCategory(@Param("category") Category category);
}
