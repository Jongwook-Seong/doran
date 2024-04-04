package com.sjw.doran.orderservice.repository;

import com.sjw.doran.orderservice.entity.Delivery;
import com.sjw.doran.orderservice.entity.DeliveryTracking;

import java.util.List;

public interface DeliveryTrackingRepositoryCustom {

    List<DeliveryTracking> findAllByDelivery(Delivery delivery);
}
