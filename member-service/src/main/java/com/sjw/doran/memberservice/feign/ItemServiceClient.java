package com.sjw.doran.memberservice.feign;

import com.sjw.doran.memberservice.vo.request.ItemListRequest;
import com.sjw.doran.memberservice.vo.response.ItemSimpleResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "item-service")//, url = "http://localhost:8000")
public interface ItemServiceClient {

    @GetMapping("/item-service/book/basket")
    List<ItemSimpleResponse> getBookBasket(@Valid @RequestBody ItemListRequest itemListRequest);
}
