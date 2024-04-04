package com.sjw.doran.orderservice.service;

import com.sjw.doran.orderservice.vo.request.OrderCreateRequest;
import com.sjw.doran.orderservice.vo.response.OrderListResponse;

import java.util.List;

public interface OrderService {

    void createOrder(String userUuid, OrderCreateRequest request);

    void cancelOrder(String userUuid, String orderUuid);

    OrderListResponse getOrderList(String userUuid);
}
