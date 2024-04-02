package com.sjw.doran.orderservice.repository;

import com.sjw.doran.orderservice.entity.Order;

import java.util.Optional;

public interface OrderRepositoryCustom {

    Optional<Order> findByOrderUuid(String orderUuid);
}
