package com.sjw.doran.memberservice.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.sjw.doran.memberservice.controller..*(..)) && args(userUuid, ..)")
    public void checkUserUuid(String userUuid) {}

    @Pointcut("execution(* com.sjw.doran.memberservice.controller.MemberController.*(..)) && args(userUuid, orderUuid, ..)")
    public void checkUserUuidAndOrderUuid(String userUuid, String orderUuid) {}

    @Pointcut("execution(* com.sjw.doran.memberservice.controller.BasketController.*(..)) && args(userUuid, itemUuid, ..)")
    public void checkUserUuidAndItemUuid(String userUuid, String itemUuid) {}
}
