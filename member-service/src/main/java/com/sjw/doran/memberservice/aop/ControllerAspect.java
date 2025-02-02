package com.sjw.doran.memberservice.aop;

import com.sjw.doran.memberservice.util.MessageUtil;
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

    @Before("com.sjw.doran.memberservice.aop.Pointcuts.checkUserUuid(userUuid)")
    public void doBeforeForCheckUserUuid(JoinPoint joinPoint, String userUuid) {
        if (userUuid.isEmpty()) {
            log.warn("WARNING from {} : {}", joinPoint.getSignature().getName(), messageUtil.getUserUuidEmptyMessage());
            throw new NoSuchElementException(messageUtil.getUserUuidEmptyMessage());
        }
    }

    @Before("com.sjw.doran.memberservice.aop.Pointcuts.checkUserUuidAndOrderUuid(userUuid, orderUuid)")
    public void doBeforeForCheckUserUuidAndOrderUuid(JoinPoint joinPoint, String userUuid, String orderUuid) {
        if (userUuid.isEmpty()) {
            log.warn("WARNING from {} : {}", joinPoint.getSignature().getName(), messageUtil.getUserUuidEmptyMessage());
            throw new NoSuchElementException(messageUtil.getUserUuidEmptyMessage());
        }
        if (orderUuid.isEmpty()) {
            log.warn("WARNING from {} : {}", joinPoint.getSignature().getName(), messageUtil.getOrderUuidEmptyMessage());
            throw new NoSuchElementException(messageUtil.getOrderUuidEmptyMessage());
        }
    }

    @Before("com.sjw.doran.memberservice.aop.Pointcuts.checkUserUuidAndItemUuid(userUuid, itemUuid)")
    public void doBeforeForCheckUserUuidAndItemUuid(JoinPoint joinPoint, String userUuid, String itemUuid) {
        if (userUuid.isEmpty()) {
            log.warn("WARNING from {} : {}", joinPoint.getSignature().getName(), messageUtil.getUserUuidEmptyMessage());
            throw new NoSuchElementException(messageUtil.getUserUuidEmptyMessage());
        }
        if (itemUuid.isEmpty()) {
            log.warn("WARNING from {} : {}", joinPoint.getSignature().getName(), messageUtil.getItemUuidEmptyMessage());
            throw new NoSuchElementException(messageUtil.getItemUuidEmptyMessage());
        }
    }
}
