package com.sjw.doran.memberservice.client;

import com.sjw.doran.memberservice.exception.FeignException;
import com.sjw.doran.memberservice.vo.response.item.ItemSimpleResponse;
import com.sjw.doran.memberservice.vo.response.order.OrderListResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ResilientItemServiceClient {

    @Autowired
    private ItemServiceClient itemServiceClient;

    private static final String BASE_CIRCUIT_BREAKER_CONFIG = "baseCircuitBreakerConfig";

    @CircuitBreaker(name = BASE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "getBookBasketFallback")
    public List<ItemSimpleResponse> getBookBasket(List<String> itemUuidList) throws InterruptedException {
        try {
            return itemServiceClient.getBookBasket(itemUuidList);
        } catch (Exception e) {
            replacementCall(itemUuidList);
            return new ArrayList<>();
        }
    }

    /** getBookBasket fallback methods **/
    public List<ItemSimpleResponse> getBookBasketFallback(String userUuid, FeignException.FeignItemServerException exception) {
        log.info("Fallback for getBookBasket: {}", exception.toString());
        return new ArrayList<>();
    }

    public List<ItemSimpleResponse> getBookBasketFallback(String userUuid, FeignException.FeignItemClientException exception) {
        log.info("Fallback for getBookBasket: {}", exception.toString());
        return new ArrayList<>();
    }

    private List<ItemSimpleResponse> getBookBasketFallback(String userUuid, CallNotPermittedException exception) {
        log.info("Fallback for getBookBasket: {}", exception.toString());
        return new ArrayList<>();
    }

    private void replacementCall(List<String> itemUuidList) throws InterruptedException {
        throw new FeignException.FeignItemServerException("feign item-server exception");
    }
}
