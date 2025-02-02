package com.sjw.doran.orderservice.aop;

import com.sjw.doran.orderservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.NoSuchElementException;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class ControllerAspect {

    private final MessageUtil messageUtil;

    @Before("com.sjw.doran.orderservice.aop.Pointcuts.checkUserUuid(userUuid)")
    public void doBeforeForCheckUserUuid(JoinPoint joinPoint, String userUuid) {
        if (userUuid.isEmpty()) {
            log.warn("WARNING from {} : {}", joinPoint.getSignature().getName(), messageUtil.getUserUuidEmptyErrorMessage());
            throw new NoSuchElementException(messageUtil.getUserUuidEmptyErrorMessage());
        }
    }

    @Before("com.sjw.doran.orderservice.aop.Pointcuts.checkUserUuidAndOrderUuid(userUuid, orderUuid)")
    public void doBeforeForCheckUserUuidAndOrderUuid(JoinPoint joinPoint, String userUuid, String orderUuid) {
        if (userUuid.isEmpty()) {
            log.warn("WARNING from {} : {}", joinPoint.getSignature().getName(), messageUtil.getUserUuidEmptyErrorMessage());
            throw new NoSuchElementException(messageUtil.getUserUuidEmptyErrorMessage());
        }
        if (orderUuid.isEmpty()) {
            log.warn("WARNING from {} : {}", joinPoint.getSignature().getName(), messageUtil.getOrderUuidEmptyErrorMessage());
            throw new NoSuchElementException(messageUtil.getOrderUuidEmptyErrorMessage());
        }
    }

    @Before("com.sjw.doran.orderservice.aop.Pointcuts.checkOrderUuid(orderUuid)")
    public void doBeforeForCheckOrderUuid(JoinPoint joinPoint, String orderUuid) {
        if (orderUuid.isEmpty()) {
            log.warn("WARNING from {} : {}", joinPoint.getSignature().getName(), messageUtil.getOrderUuidEmptyErrorMessage());
            throw new NoSuchElementException(messageUtil.getOrderUuidEmptyErrorMessage());
        }
    }
}
