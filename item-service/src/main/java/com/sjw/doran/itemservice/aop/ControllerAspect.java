package com.sjw.doran.itemservice.aop;

import com.sjw.doran.itemservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class ControllerAspect {

    private final MessageUtil messageUtil;

    @Before("com.sjw.doran.itemservice.aop.Pointcuts.checkItemUuid(itemUuid)")
    public void doBeforeForCheckItemUuid(JoinPoint joinPoint, String itemUuid) {
        if (itemUuid.isEmpty()) {
            log.warn("WARNING from {} : {}", joinPoint.getSignature().getName(), messageUtil.getItemUuidEmptyErrorMessage());
            throw new NoSuchElementException(messageUtil.getItemUuidEmptyErrorMessage());
        }
    }

    @Before("com.sjw.doran.itemservice.aop.Pointcuts.checkItemUuidList(itemUuidList)")
    public void doBeforeForCheckItemUuidList(JoinPoint joinPoint, List<String> itemUuidList) {
        if (itemUuidList.isEmpty()) {
            log.warn("WARNING from {} : {}", joinPoint.getSignature().getName(), messageUtil.getItemUuidEmptyErrorMessage());
            throw new NoSuchElementException(messageUtil.getItemUuidEmptyErrorMessage());
        }
    }
}
