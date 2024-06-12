package com.sjw.doran.orderservice.exception;

public class RetryException extends RuntimeException {
    public RetryException(String message) {
        super(message);
    }
}
