package com.sjw.doran.authservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_IDENTITY(HttpStatus.CONFLICT, "User ID(Account) is duplicated"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),
    INVALID_USER_UUID(HttpStatus.UNAUTHORIZED, "User UUID is invalid"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token is invalid"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token is invalid"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Permission is invalid");

    private HttpStatus status;
    private String message;
}
