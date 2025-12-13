package com.sjw.doran.authservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "User ID is duplicated"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),
    INVALID_USER_UUID(HttpStatus.UNAUTHORIZED, "User UUID is invalid"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    MEMBER_CREATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Member Creation Error"),
    MEMBER_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Member Delete Error"),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token is invalid"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token is invalid"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Article not founded"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Permission is invalid");

    private HttpStatus status;
    private String message;
}
