package com.sjw.doran.orderservice.client;

import com.sjw.doran.orderservice.vo.response.ItemSimpleWithQuantityResponse;
import com.sjw.doran.orderservice.vo.response.ItemSimpleWithoutPriceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "item-service")
public interface ItemServiceClient {

    @GetMapping("/orderitems")
    List<ItemSimpleWithQuantityResponse> getOrderItems(@RequestParam("itemUuidList") List<String> itemUuidList);

    @PutMapping("/orderitems")
    void orderItems(@RequestParam("itemUuidList") List<String> itemUuidList, @RequestParam("countList") List<Integer> countList);

    @PutMapping("/orderitems/cancel")
    void cancelOrderItems(@RequestParam("itemUuidList") List<String> itemUuidList, @RequestParam("countList") List<Integer> countList);

    @GetMapping("/items/simple")
    List<ItemSimpleWithoutPriceResponse> getItemSimpleWithoutPrice(@RequestParam("itemUuidList") List<String> itemUuidList);
}
