package com.sjw.doran.orderservice.service;

import com.sjw.doran.orderservice.vo.request.OrderCreateRequest;

import java.util.List;

public interface OrderService {

    void createOrder(String userUuid, OrderCreateRequest request);
}
