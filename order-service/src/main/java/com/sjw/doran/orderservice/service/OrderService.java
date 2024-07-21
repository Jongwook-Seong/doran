package com.sjw.doran.orderservice.service;

import com.sjw.doran.orderservice.vo.ItemSimpleInfo;
import com.sjw.doran.orderservice.vo.request.DeliveryStatusPostRequest;
import com.sjw.doran.orderservice.vo.request.OrderCreateRequest;
import com.sjw.doran.orderservice.vo.response.DeliveryTrackingResponse;
import com.sjw.doran.orderservice.vo.response.OrderDetailResponse;
import com.sjw.doran.orderservice.vo.response.OrderListResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderService {

    void createOrder(String userUuid, OrderCreateRequest request) throws InterruptedException;

    void cancelOrder(String userUuid, String orderUuid) throws InterruptedException;

    OrderListResponse getOrderList(String userUuid) throws InterruptedException;

    OrderDetailResponse getOrderDetail(String userUuid, String orderUuid);

    CompletableFuture<OrderDetailResponse> getOrderDetailAsync(String userUuid, String orderUuid);

    DeliveryTrackingResponse getDeliveryTrackingInfo(String userUuid, String orderUuid);

    void updateDeliveryInfo(String orderUuid, DeliveryStatusPostRequest request);
}
