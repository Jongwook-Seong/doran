package com.sjw.doran.orderservice.repository;

import com.sjw.doran.orderservice.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryCustom {

    Optional<Order> findByOrderUuid(String orderUuid);

    List<Order> findAllByUserUuid(String userUuid);

    void updateOrderStatusAsCancel(String userUuid, String orderUuid);
}
