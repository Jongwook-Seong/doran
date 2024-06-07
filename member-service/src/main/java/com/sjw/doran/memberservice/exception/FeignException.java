package com.sjw.doran.memberservice.exception;

public class FeignException {

    public static class FeignServerException extends RuntimeException {
        public FeignServerException(String message) {
            super(message);
        }
    }

    public static class FeignClientException extends RuntimeException {
        public FeignClientException(String message) {
            super(message);
        }
    }
}
