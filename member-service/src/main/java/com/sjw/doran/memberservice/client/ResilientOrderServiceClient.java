package com.sjw.doran.memberservice.client;

import com.sjw.doran.memberservice.exception.IgnoreException;
import com.sjw.doran.memberservice.exception.RecordException;
import com.sjw.doran.memberservice.vo.response.order.DeliveryTrackingResponse;
import com.sjw.doran.memberservice.vo.response.order.OrderDetailResponse;
import com.sjw.doran.memberservice.vo.response.order.OrderListResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ResilientOrderServiceClient {

    @Autowired
    private OrderServiceClient orderServiceClient;

    private static final String BASE_CIRCUIT_BREAKER_CONFIG = "baseCircuitBreakerConfig";
    private static final String BASE_RETRY_CONFIG = "baseRetryConfig";

    @Retry(name = BASE_RETRY_CONFIG)
    @CircuitBreaker(name = BASE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "inquireOrderListFallback")
    public OrderListResponse inquireOrderList(String userUuid) throws InterruptedException {
        try {
            return orderServiceClient.inquireOrderList(userUuid);
        } catch (Exception e) {
            replacementCall(userUuid);
            return null;
        }
    }

    @Retry(name = BASE_RETRY_CONFIG)
    @CircuitBreaker(name = BASE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "inquireOrderDetailFallback")
    public OrderDetailResponse inquireOrderDetail(String userUuid, String orderUuid) throws InterruptedException {
        try {
            return orderServiceClient.inquireOrderDetail(userUuid, orderUuid);
        } catch (Exception e) {
            replacementCall(userUuid);
            return null;
        }
    }

    @Retry(name = BASE_RETRY_CONFIG)
    @CircuitBreaker(name = BASE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "inquireDeliveryTrackingFallback")
    public DeliveryTrackingResponse inquireDeliveryTracking(String userUuid, String orderUuid) throws InterruptedException {
        try {
            return orderServiceClient.inquireDeliveryTracking(userUuid, orderUuid);
        } catch (Exception e) {
            replacementCall(userUuid);
            return null;
        }
    }

    /** inquireOrderList fallback methods **/
    public OrderListResponse inquireOrderListFallback(String userUuid, RecordException exception) {
        log.info("Fallback for inquireOrderList: {}", exception.toString());
        return new OrderListResponse();
    }

    public OrderListResponse inquireOrderListFallback(String userUuid, IgnoreException exception) {
        log.info("Fallback for inquireOrderList: {}", exception.toString());
        return new OrderListResponse();
    }

    public OrderListResponse inquireOrderListFallback(String userUuid, CallNotPermittedException exception) {
        log.info("Fallback for inquireOrderList: {}", exception.toString());
        return new OrderListResponse();
    }

    /** inquireOrderDetail fallback methods **/
    public OrderDetailResponse inquireOrderDetailFallback(String userUuid, String orderUuid, RecordException exception) {
        log.info("Fallback for inquireOrderDetail: {}", exception.toString());
        return new OrderDetailResponse();
    }

    public OrderDetailResponse inquireOrderDetailFallback(String userUuid, String orderUuid, IgnoreException exception) {
        log.info("Fallback for inquireOrderDetail: {}", exception.toString());
        return new OrderDetailResponse();
    }

    public OrderDetailResponse inquireOrderDetailFallback(String userUuid, String orderUuid, CallNotPermittedException exception) {
        log.info("Fallback for inquireOrderDetail: {}", exception.toString());
        return new OrderDetailResponse();
    }

    /** inquireDeliveryTracking fallback methods **/
    public DeliveryTrackingResponse inquireDeliveryTrackingFallback(String userUuid, String orderUuid, RecordException exception) {
        log.info("Fallback for inquireDeliveryTracking: {}", exception.toString());
        return new DeliveryTrackingResponse();
    }

    public DeliveryTrackingResponse inquireDeliveryTrackingFallback(String userUuid, String orderUuid, IgnoreException exception) {
        log.info("Fallback for inquireDeliveryTracking: {}", exception.toString());
        return new DeliveryTrackingResponse();
    }

    public DeliveryTrackingResponse inquireDeliveryTrackingFallback(String userUuid, String orderUuid, CallNotPermittedException exception) {
        log.info("Fallback for inquireDeliveryTracking: {}", exception.toString());
        return new DeliveryTrackingResponse();
    }

    private void replacementCall(String param) throws InterruptedException {
        throw new RecordException("record exception");
    }
}
