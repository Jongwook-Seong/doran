package com.sjw.doran.memberservice.client;

import com.sjw.doran.memberservice.vo.response.item.ItemSimpleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "item-service")
public interface ItemServiceClient {

    @GetMapping("/item-service/book/basket")
    List<ItemSimpleResponse> getBookBasket(@RequestParam("itemUuidList") List<String> itemUuidList);
}
