package com.sjw.doran.memberservice.controller;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.service.BasketService;
import com.sjw.doran.memberservice.service.MemberService;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @PostMapping("/addItem")
    @Operation(summary = "장바구니 상품 추가", description = "장바구니에 해당 상품을 저장합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Success",
//                    content = {@Content(schema = @Schema(implementation = Member.class))}),
//            @ApiResponse(responseCode = "500", description = "Fail")
//    })
    public void setBasketItem(@RequestHeader String userUuid, @RequestBody BasketItemCreateRequest basketItemCreateRequest) {
        basketService.addBasketItem(userUuid, basketItemCreateRequest);
    }
}
