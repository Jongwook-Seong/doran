package com.sjw.doran.orderservice.repository;

import com.sjw.doran.orderservice.entity.Delivery;
import com.sjw.doran.orderservice.entity.DeliveryStatus;
import com.sjw.doran.orderservice.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryCustom {

    Optional<Order> findByOrderUuid(String orderUuid);

    Optional<Order> findByUserUuidAndOrderUuid(String userUuid, String orderUuid);

//    Optional<Order> findByUserUuidAndOrderUuidOrderByDeliveryTracking(String userUuid, String orderUuid);

    List<Order> findAllByUserUuid(String userUuid);

    void updateOrderStatusAsCancel(String userUuid, String orderUuid);

    Delivery updateDeliveryStatus(String orderUuid, DeliveryStatus deliveryStatus);
}
