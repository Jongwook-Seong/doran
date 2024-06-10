package com.sjw.doran.apigatewayservice.exception;

public class IgnoreException extends RuntimeException {
    public IgnoreException(String message) {
        super(message);
    }
}
