package com.sjw.doran.orderservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = {"com.sjw.doran.orderservice.controller"})
public class ControllerExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> getHandler(MethodArgumentNotValidException exception, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorResult.getInstance(
                getMethodArgumentNotValidMessage(exception), request.getRequestURL().toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResult> getHandler(MissingRequestHeaderException exception, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorResult.getInstance(exception.getMessage(), request.getRequestURL().toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResult> getHandler(MissingServletRequestParameterException exception, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorResult.getInstance(exception.getMessage(), request.getRequestURL().toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResult> getHandler(RuntimeException exception, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorResult.getInstance(exception.getMessage(), request.getRequestURL().toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResult> getHandler(NoSuchElementException exception, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorResult.getInstance(exception.getMessage(), request.getRequestURL().toString()), HttpStatus.BAD_REQUEST);
    }

    private List<String> getMethodArgumentNotValidMessage(MethodArgumentNotValidException exception) {
        ArrayList<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errorMessages.add("[" + fieldError.getField() + "]는(은) " + fieldError.getDefaultMessage() +
                    "[ 입력된 값 : " + fieldError.getRejectedValue() + " ]");
        }
        return errorMessages;
    }
}
