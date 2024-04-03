package com.sjw.doran.orderservice.repository;

import com.sjw.doran.orderservice.entity.Delivery;
import com.sjw.doran.orderservice.entity.Order;

public interface DeliveryRepositoryCustom {

    Delivery findByOrder(Order order);
}
