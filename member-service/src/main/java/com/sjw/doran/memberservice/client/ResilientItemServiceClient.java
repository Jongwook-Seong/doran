package com.sjw.doran.memberservice.client;

import com.sjw.doran.memberservice.exception.FeignException;
import com.sjw.doran.memberservice.exception.IgnoreException;
import com.sjw.doran.memberservice.exception.RecordException;
import com.sjw.doran.memberservice.vo.response.item.ItemSimpleResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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
    private static final String BASE_RETRY_CONFIG = "baseRetryConfig";

    @Retry(name = BASE_RETRY_CONFIG)
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
    public List<ItemSimpleResponse> getBookBasketFallback(List<String> itemUuidList, RecordException exception) {
        log.info("Fallback for getBookBasket: {}", exception.toString());
        return new ArrayList<>();
    }

    public List<ItemSimpleResponse> getBookBasketFallback(List<String> itemUuidList, IgnoreException exception) {
        log.info("Fallback for getBookBasket: {}", exception.toString());
        return new ArrayList<>();
    }

    private List<ItemSimpleResponse> getBookBasketFallback(List<String> itemUuidList, CallNotPermittedException exception) {
        log.info("Fallback for getBookBasket: {}", exception.toString());
        return new ArrayList<>();
    }

    private void replacementCall(List<String> itemUuidList) throws InterruptedException {
        throw new RecordException("record exception");
    }
}
