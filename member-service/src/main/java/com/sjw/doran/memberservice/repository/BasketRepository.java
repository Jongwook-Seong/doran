package com.sjw.doran.memberservice.repository;

import com.sjw.doran.memberservice.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long>, BasketRepositoryCustom {
}
