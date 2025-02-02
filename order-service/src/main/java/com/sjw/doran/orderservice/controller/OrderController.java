package com.sjw.doran.orderservice.controller;

import com.sjw.doran.orderservice.service.OrderService;
import com.sjw.doran.orderservice.vo.request.DeliveryStatusPostRequest;
import com.sjw.doran.orderservice.vo.request.OrderCreateRequest;
import com.sjw.doran.orderservice.vo.response.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /** 주문 생성하기 **/
    @PostMapping("/orders")
    public ResponseEntity<Void> createOrder(@RequestHeader("userUuid") String userUuid, @Valid @RequestBody OrderCreateRequest request) throws InterruptedException {
        orderService.createOrder(userUuid, request);
        return ResponseEntity.ok().build();
    }

    /** 주문 취소하기 **/
    @PutMapping("/cancel")
    public ResponseEntity<Void> cancelOrder(@RequestHeader("userUuid") String userUuid, @RequestParam("orderUuid") String orderUuid) throws InterruptedException {
        orderService.cancelOrder(userUuid, orderUuid);
        return ResponseEntity.ok().build();
    }

    /** 주문 목록 조회하기 **/
    @GetMapping("/list")
    public ResponseEntity<OrderListResponse> inquireOrderList(@RequestHeader("userUuid") String userUuid) throws InterruptedException {
        OrderListResponse orderListResponse = orderService.getOrderList(userUuid);
        return new ResponseEntity<>(orderListResponse, HttpStatus.OK);
    }

    /** 최근 주문 목록 간략 조회하기 **/

    /** 주문 상세 조회하기 **/
    @GetMapping("/detail")
    public ResponseEntity<OrderDetailResponse> inquireOrderDetail(@RequestHeader("userUuid") String userUuid, @RequestParam("orderUuid") String orderUuid) {
        OrderDetailResponse orderDetailResponse = orderService.getOrderDetail(userUuid, orderUuid);
        return new ResponseEntity<>(orderDetailResponse, HttpStatus.OK);
    }

    /** 주문 상세 조회하기 - @Async, CompletableFuture 테스트 적용 **/
    @GetMapping("/detail/async")
    public CompletableFuture<ResponseEntity<OrderDetailResponse>> inquireOrderDetailAsync(@RequestHeader("userUuid") String userUuid, @RequestParam("orderUuid") String orderUuid) {
        return orderService.getOrderDetailAsync(userUuid, orderUuid).thenApply(ResponseEntity::ok);
    }

    /** 배송 추적 조회하기 **/
    @GetMapping("/delivery/tracking")
    public ResponseEntity<DeliveryTrackingResponse> inquireDeliveryTracking(@RequestHeader("userUuid") String userUuid, @RequestParam("orderUuid") String orderUuid) {
        DeliveryTrackingResponse deliveryTrackingInfo = orderService.getDeliveryTrackingInfo(userUuid, orderUuid);
        return new ResponseEntity<>(deliveryTrackingInfo, HttpStatus.OK);
    }

    /** 배송 상태 갱신하기 **/
    @PostMapping("/delivery/status")
    public ResponseEntity<Void> updateDeliveryInfo(@RequestParam("orderUuid") String orderUuid, @Valid @RequestBody DeliveryStatusPostRequest request) {
        orderService.updateDeliveryInfo(orderUuid, request);
        return ResponseEntity.ok().build();
    }
}
