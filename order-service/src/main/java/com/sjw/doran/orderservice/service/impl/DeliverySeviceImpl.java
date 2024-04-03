package com.sjw.doran.orderservice.service.impl;

import com.sjw.doran.orderservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliverySeviceImpl {

    private final DeliveryRepository deliveryRepository;
}
