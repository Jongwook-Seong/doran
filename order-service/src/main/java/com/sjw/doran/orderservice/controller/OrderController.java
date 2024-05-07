package com.sjw.doran.orderservice.controller;

import com.sjw.doran.orderservice.client.ItemServiceClient;
import com.sjw.doran.orderservice.service.OrderService;
import com.sjw.doran.orderservice.util.MessageUtil;
import com.sjw.doran.orderservice.vo.ItemSimpleInfo;
import com.sjw.doran.orderservice.vo.request.DeliveryStatusPostRequest;
import com.sjw.doran.orderservice.vo.request.OrderCreateRequest;
import com.sjw.doran.orderservice.vo.response.DeliveryTrackingResponse;
import com.sjw.doran.orderservice.vo.response.OrderDetailResponse;
import com.sjw.doran.orderservice.vo.response.OrderListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ItemServiceClient itemServiceClient;
    private final MessageUtil messageUtil;

    /** 주문 생성하기 **/
    @PostMapping("/orders")
    public ResponseEntity<Void> createOrder(@RequestHeader("userUuid") String userUuid, @Valid @RequestBody OrderCreateRequest request) {
        if (userUuid.isEmpty()) {
            throw new NoSuchElementException(messageUtil.getUserUuidEmptyErrorMessage());
        }

        List<ItemSimpleInfo> itemSimpleInfoList = request.getItemSimpleInfoList();
        List<String> itemUuidList = new ArrayList<>();
        List<Integer> itemCountList = new ArrayList<>();
        for (ItemSimpleInfo itemSimpleInfo : itemSimpleInfoList) {
            itemUuidList.add(itemSimpleInfo.getItemUuid());
            itemCountList.add(itemSimpleInfo.getCount());
        }

        orderService.createOrder(userUuid, request);
        itemServiceClient.orderItems(itemUuidList, itemCountList);
        return ResponseEntity.ok().build();
    }

    /** 주문 취소하기 **/
    @PutMapping("/cancel")
    public ResponseEntity<Void> cancelOrder(@RequestHeader("userUuid") String userUuid, @RequestParam("orderUuid") String orderUuid) {
        if (userUuid.isEmpty()) {
            throw new NoSuchElementException(messageUtil.getUserUuidEmptyErrorMessage());
        } else if (orderUuid.isEmpty()) {
            throw new NoSuchElementException(messageUtil.getOrderUuidEmptyErrorMessage());
        }
        orderService.cancelOrder(userUuid, orderUuid);
        return ResponseEntity.ok().build();
    }

    /** 주문 목록 조회하기 **/
    @GetMapping("/list")
    public ResponseEntity<OrderListResponse> inquireOrderList(@RequestHeader("userUuid") String userUuid) {
        if (userUuid.isEmpty()) {
            throw new NoSuchElementException(messageUtil.getUserUuidEmptyErrorMessage());
        }
        OrderListResponse orderListResponse = orderService.getOrderList(userUuid);
        return new ResponseEntity<>(orderListResponse, HttpStatus.OK);
    }

    /** 최근 주문 목록 간략 조회하기 **/

    /** 주문 상세 조회하기 **/
    @GetMapping("/detail")
    public ResponseEntity<OrderDetailResponse> inquireOrderDetail(@RequestHeader("userUuid") String userUuid, @RequestParam("orderUuid") String orderUuid) {
        if (userUuid.isEmpty()) {
            throw new NoSuchElementException(messageUtil.getUserUuidEmptyErrorMessage());
        } else if (orderUuid.isEmpty()) {
            throw new NoSuchElementException(messageUtil.getOrderUuidEmptyErrorMessage());
        }
        OrderDetailResponse orderDetailResponse = orderService.getOrderDetail(userUuid, orderUuid);
        return new ResponseEntity<>(orderDetailResponse, HttpStatus.OK);
    }

    /** 배송 추적 조회하기 **/
    @GetMapping("/delivery/tracking")
    public ResponseEntity<DeliveryTrackingResponse> inquireDeliveryTracking(@RequestHeader("userUuid") String userUuid, @RequestParam("orderUuid") String orderUuid) {
        if (userUuid.isEmpty()) {
            throw new NoSuchElementException(messageUtil.getUserUuidEmptyErrorMessage());
        } else if (orderUuid.isEmpty()) {
            throw new NoSuchElementException(messageUtil.getOrderUuidEmptyErrorMessage());
        }
        DeliveryTrackingResponse deliveryTrackingInfo = orderService.getDeliveryTrackingInfo(userUuid, orderUuid);
        return new ResponseEntity<>(deliveryTrackingInfo, HttpStatus.OK);
    }

    /** 배송 상태 갱신하기 **/
    @PostMapping("/delivery/status")
    public ResponseEntity<Void> updateDeliveryInfo(@RequestParam("orderUuid") String orderUuid, @Valid @RequestBody DeliveryStatusPostRequest request) {
        if (orderUuid.isEmpty()) {
            throw new NoSuchElementException(messageUtil.getOrderUuidEmptyErrorMessage());
        }
        orderService.updateDeliveryInfo(orderUuid, request);
        return ResponseEntity.ok().build();
    }
}
