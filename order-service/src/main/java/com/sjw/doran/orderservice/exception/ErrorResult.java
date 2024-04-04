package com.sjw.doran.orderservice.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class ErrorResult {

    private String errorMessage;
    private List<String> errorMessages;
    private String requestUrl;
    private LocalDateTime timestamp;

    public static ErrorResult getInstance(String errorMessage, String requestUrl) {
        return ErrorResult.builder()
                .errorMessage(errorMessage)
                .requestUrl(requestUrl)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResult getInstance(List<String> errorMessages, String requestUrl) {
        return ErrorResult.builder()
                .errorMessages(errorMessages)
                .requestUrl(requestUrl)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
