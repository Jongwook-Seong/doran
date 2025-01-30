package com.sjw.doran.orderservice.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.sjw.doran.orderservice.controller..*(..)) && args(userUuid, ..)")
    public void checkUserUuid(String userUuid) {}

    @Pointcut("execution(* com.sjw.doran.orderservice.controller..*(..)) && args(userUuid, orderUuid, ..)")
    public void checkUserUuidAndOrderUuid(String userUuid, String orderUuid) {}

    @Pointcut("execution(* com.sjw.doran.orderservice.controller.OrderController.updateDeliveryInfo(..)) && args(orderUuid, ..)")
    public void checkOrderUuid(String orderUuid) {}
}
