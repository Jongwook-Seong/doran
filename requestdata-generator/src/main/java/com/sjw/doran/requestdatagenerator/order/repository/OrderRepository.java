package com.sjw.doran.requestdatagenerator.order.repository;

import com.sjw.doran.requestdatagenerator.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
