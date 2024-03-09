package com.sjw.doran.memberservice.repository;

import com.sjw.doran.memberservice.entity.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketItemRepository extends JpaRepository<BasketItem, Long>, BasketItemRepositoryCustom {
}
