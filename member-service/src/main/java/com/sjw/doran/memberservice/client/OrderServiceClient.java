package com.sjw.doran.memberservice.client;

import com.sjw.doran.memberservice.vo.response.order.DeliveryTrackingResponse;
import com.sjw.doran.memberservice.vo.response.order.OrderDetailResponse;
import com.sjw.doran.memberservice.vo.response.order.OrderListResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service")
public interface OrderServiceClient {
    @GetMapping("/list")
    OrderListResponse inquireOrderList(@RequestHeader("userUuid") String userUuid);
    @GetMapping("/detail")
    OrderDetailResponse inquireOrderDetail(@RequestHeader("userUuid") String userUuid, @RequestParam("orderUuid") String orderUuid);
    @GetMapping("/delivery/tracking")
    DeliveryTrackingResponse inquireDeliveryTracking(@RequestHeader("userUuid") String userUuid, @RequestParam("orderUuid") String orderUuid);
}
