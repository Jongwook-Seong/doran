package com.sjw.doran.memberservice.exception;

public class FeignException {

    public static class FeignOrderServerException extends RuntimeException {
        public FeignOrderServerException(String message) {
            super(message);
        }
    }

    public static class FeignOrderClientException extends RuntimeException {
        public FeignOrderClientException(String message) {
            super(message);
        }
    }

    public static class FeignItemServerException extends RuntimeException {
        public FeignItemServerException(String message) {
            super(message);
        }
    }

    public static class FeignItemClientException extends RuntimeException {
        public FeignItemClientException(String message) {
            super(message);
        }
    }
}
