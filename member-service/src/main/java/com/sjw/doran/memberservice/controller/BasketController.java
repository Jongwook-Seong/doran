package com.sjw.doran.memberservice.controller;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.service.BasketService;
import com.sjw.doran.memberservice.service.MemberService;
import com.sjw.doran.memberservice.util.MessageUtil;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import com.sjw.doran.memberservice.vo.response.BasketItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member/basket")
@RequiredArgsConstructor
@Tag(name = "Member Service", description = "Basket in Member Service Swagger API")
public class BasketController {

    private final BasketService basketService;
    private final MessageUtil messageUtil;

    @PostMapping("/addItem")
    @Operation(summary = "장바구니 상품 추가", description = "장바구니에 해당 상품을 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "Fail")
    })
    public ResponseEntity setBasketItem(@RequestHeader String userUuid, @RequestBody BasketItemCreateRequest basketItemCreateRequest) {
        basketService.addBasketItem(userUuid, basketItemCreateRequest);
        return ResponseEntity.ok(BasketItemResponse.getInstance(basketItemCreateRequest.getItemUuid(), messageUtil.getBasketItemCreateMessage()));
    }

    @DeleteMapping("/deleteItem")
    @Operation(summary = "장바구니 상품 삭제", description = "장바구니에서 해당 상품을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "Fail")
    })
    public ResponseEntity deleteBasketItem(@RequestHeader String userUuid, @RequestParam("itemUuid") String itemUuid) {
        basketService.deleteBasketItem(userUuid, itemUuid);
        return ResponseEntity.ok(BasketItemResponse.getInstance(itemUuid, messageUtil.getBasketItemDeleteMessage()));
    }
}
