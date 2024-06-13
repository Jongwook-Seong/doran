package com.sjw.doran.orderservice.client;

import com.sjw.doran.orderservice.exception.RecordException;
import com.sjw.doran.orderservice.exception.IgnoreException;
import com.sjw.doran.orderservice.vo.response.ItemSimpleWithQuantityResponse;
import com.sjw.doran.orderservice.vo.response.ItemSimpleWithoutPriceResponse;
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
    @CircuitBreaker(name = BASE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "getOrderItemsFallback")
    public List<ItemSimpleWithQuantityResponse> getOrderItems(List<String> itemUuidList) throws InterruptedException {
        try {
            return itemServiceClient.getOrderItems(itemUuidList);
        } catch (Exception e) {
            replacementCall("getOrderItems");
//            return new ArrayList<>();
            return null;
        }
    }

    @Retry(name = BASE_RETRY_CONFIG)
    @CircuitBreaker(name = BASE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "orderItemsFallback")
    public void orderItems(List<String> itemUuidList, List<Integer> countList) throws InterruptedException {
        try {
            itemServiceClient.orderItems(itemUuidList, countList);
        } catch (Exception e) {
            replacementCall("orderItems");
        }
    }

    @Retry(name = BASE_RETRY_CONFIG)
    @CircuitBreaker(name = BASE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "cancelOrderItemsFallback")
    public void cancelOrderItems(List<String> itemUuidList, List<Integer> countList) throws InterruptedException {
        try {
            itemServiceClient.cancelOrderItems(itemUuidList, countList);
        } catch (Exception e) {
            replacementCall("cancelOrderItems");
        }
    }

    @Retry(name = BASE_RETRY_CONFIG)
    @CircuitBreaker(name = BASE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "getItemSimpleWithoutPriceFallback")
    public List<ItemSimpleWithoutPriceResponse> getItemSimpleWithoutPrice(List<String> itemUuidList) throws InterruptedException {
        try {
            return itemServiceClient.getItemSimpleWithoutPrice(itemUuidList);
        } catch (Exception e) {
            replacementCall("getOrderItems");
//            return new ArrayList<>();
            return null;
        }
    }

    /** getOrderItems fallback methods **/
    public List<ItemSimpleWithQuantityResponse> getOrderItemsFallback(List<String> itemUuidList, RecordException exception) {
        log.info("Fallback for getOrderItems: {}", exception.toString());
        return new ArrayList<>();
    }

    public List<ItemSimpleWithQuantityResponse> getOrderItemsFallback(List<String> itemUuidList, IgnoreException exception) {
        log.info("Fallback for getOrderItems: {}", exception.toString());
        return new ArrayList<>();
    }

    public List<ItemSimpleWithQuantityResponse> getOrderItemsFallback(List<String> itemUuidList, CallNotPermittedException exception) {
        log.info("Fallback for getOrderItems: {}", exception.toString());
        return new ArrayList<>();
    }

    /** orderItems fallback methods **/
    public void orderItemsFallback(List<String> itemUuidList, List<Integer> countList, RecordException exception) {
        log.info("Fallback for orderItems: {}", exception.toString());
        throw exception;
    }

    public void orderItemsFallback(List<String> itemUuidList, List<Integer> countList, IgnoreException exception) {
        log.info("Fallback for orderItems: {}", exception.toString());
    }

    public void orderItemsFallback(List<String> itemUuidList, List<Integer> countList, CallNotPermittedException exception) {
        log.info("Fallback for orderItems: {}", exception.toString());
        throw exception;
    }

    /** cancelOrderItems fallback methods **/
    public void cancelOrderItemsFallback(List<String> itemUuidList, List<Integer> countList, RecordException exception) {
        log.info("Fallback for cancelOrderItems: {}", exception.toString());
        throw exception;
    }

    public void cancelOrderItemsFallback(List<String> itemUuidList, List<Integer> countList, IgnoreException exception) {
        log.info("Fallback for cancelOrderItems: {}", exception.toString());
    }

    public void cancelOrderItemsFallback(List<String> itemUuidList, List<Integer> countList, CallNotPermittedException exception) {
        log.info("Fallback for cancelOrderItems: {}", exception.toString());
        throw exception;
    }

    /** getItemSimpleWithoutPrice fallback methods **/
    public List<ItemSimpleWithoutPriceResponse> getItemSimpleWithoutPriceFallback(List<String> itemUuidList, RecordException exception) {
        log.info("Fallback for getItemSimpleWithoutPrice: {}", exception.toString());
        return new ArrayList<>();
    }

    public List<ItemSimpleWithoutPriceResponse> getItemSimpleWithoutPriceFallback(List<String> itemUuidList, IgnoreException exception) {
        log.info("Fallback for getItemSimpleWithoutPrice: {}", exception.toString());
        return new ArrayList<>();
    }

    public List<ItemSimpleWithoutPriceResponse> getItemSimpleWithoutPriceFallback(List<String> itemUuidList, CallNotPermittedException exception) {
        log.info("Fallback for getItemSimpleWithoutPrice: {}", exception.toString());
        return new ArrayList<>();
    }

    private void replacementCall(String param) throws InterruptedException {
        throw new RecordException("record exception");
    }
}
