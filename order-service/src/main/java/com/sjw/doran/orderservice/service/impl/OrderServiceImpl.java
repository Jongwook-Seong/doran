package com.sjw.doran.orderservice.service.impl;

import com.sjw.doran.orderservice.repository.OrderRepository;
import com.sjw.doran.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
}
