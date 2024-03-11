package com.sjw.doran.memberservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.sjw.doran.memberservice.controller")
public class ControllerExceptionAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResult> getHandler(DataIntegrityViolationException exception, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorResult.getInstance(exception.getMessage(), request.getRequestURL().toString()), HttpStatus.BAD_REQUEST);
    }
}
