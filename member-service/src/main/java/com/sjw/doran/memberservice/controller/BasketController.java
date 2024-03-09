package com.sjw.doran.memberservice.controller;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.service.BasketService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @PostMapping("/basket/addItem")
    @Operation(summary = "장바구니 상품 추가", description = "장바구니에 해당 상품을 저장합니다.")
    public void setBasketItem(@RequestHeader String memberUuid, @RequestParam String itemUuid) {
//        Member member = memberService.findMember(memberUuid);
//        Basket basket = basketService.findBasket(member);
//        basketItemService.setBasketItem(basket, itemUuid);
    }
}
