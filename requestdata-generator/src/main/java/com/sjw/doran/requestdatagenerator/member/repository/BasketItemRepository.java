package com.sjw.doran.requestdatagenerator.member.repository;

import com.sjw.doran.requestdatagenerator.member.entity.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {
}
