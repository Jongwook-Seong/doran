package com.sjw.doran.itemservice.aop;

import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

public class Pointcuts {

    @Pointcut("execution(* com.sjw.doran.itemservice.controller..*(..)) && " +
            "!execution(* com.sjw.doran.itemservice.controller.ItemController.bookSearch(..)) && args(itemUuid, ..)")
    public void checkItemUuid(String itemUuid) {}

    @Pointcut("execution(* com.sjw.doran.itemservice.controller..*(..)) && args(itemUuidList, ..)")
    public void checkItemUuidList(List<String> itemUuidList) {}
}
