package com.sjw.doran.orderservice.service;

import com.sjw.doran.orderservice.vo.ItemSimpleInfo;
import com.sjw.doran.orderservice.vo.request.DeliveryStatusPostRequest;
import com.sjw.doran.orderservice.vo.request.OrderCreateRequest;
import com.sjw.doran.orderservice.vo.response.DeliveryTrackingResponse;
import com.sjw.doran.orderservice.vo.response.OrderDetailResponse;
import com.sjw.doran.orderservice.vo.response.OrderListResponse;

import java.util.List;

public interface OrderService {

    void createOrder(String userUuid, OrderCreateRequest request);

    List<ItemSimpleInfo> cancelOrder(String userUuid, String orderUuid);

    OrderListResponse getOrderList(String userUuid);

    OrderDetailResponse getOrderDetail(String userUuid, String orderUuid);

    DeliveryTrackingResponse getDeliveryTrackingInfo(String userUuid, String orderUuid);

    void updateDeliveryInfo(String orderUuid, DeliveryStatusPostRequest request);
}
