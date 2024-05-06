package com.sjw.doran.orderservice.client;

import com.sjw.doran.orderservice.vo.response.ItemSimpleWithQuantityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "item-service")
public interface ItemServiceClient {

    @GetMapping("/item-service/orderitems")
    List<ItemSimpleWithQuantityResponse> getOrderItems(@RequestParam("itemUuidList") List<String> itemUuidList);
}
