package com.sjw.doran.orderservice.controller;

import com.sjw.doran.orderservice.service.OrderService;
import com.sjw.doran.orderservice.vo.request.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /** 주문 생성하기 **/
    @PostMapping("/orders")
    public ResponseEntity<Void> createOrder(@RequestHeader("userUuid") String userUuid, @RequestBody OrderCreateRequest request) {
        orderService.createOrder(userUuid, request);
        return ResponseEntity.accepted().build();
    }

    /** 주문 취소하기 **/
    @PutMapping("/cancel")
    public ResponseEntity<Void> createOrder(@RequestHeader("userUuid") String userUuid, @RequestParam("orderUuid") String orderUuid) {
        return ResponseEntity.accepted().build();
    }

    /** 주문 목록 조회하기 **/

    /** 주문 상세 조회하기 **/

    /** 배송 조회하기 **/

    /** 배송 상태 갱신하기 **/
}
