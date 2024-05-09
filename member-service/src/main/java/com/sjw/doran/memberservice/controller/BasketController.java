package com.sjw.doran.memberservice.controller;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.service.BasketItemService;
import com.sjw.doran.memberservice.service.BasketService;
import com.sjw.doran.memberservice.service.MemberService;
import com.sjw.doran.memberservice.util.MessageUtil;
import com.sjw.doran.memberservice.util.ModelMapperUtil;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import com.sjw.doran.memberservice.vo.response.BasketItemResponse;
import com.sjw.doran.memberservice.vo.response.ItemSimpleWithCountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
@Tag(name = "Member Service", description = "Basket in Member Service Swagger API")
public class BasketController {

    private final MemberService memberService;
    private final BasketService basketService;
    private final BasketItemService basketItemService;
    private final MessageUtil messageUtil;
    private final ModelMapperUtil modelMapperUtil;

    @PostMapping("/addItem")
    @Operation(summary = "장바구니 상품 추가", description = "장바구니에 해당 상품을 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "Fail")
    })
    public ResponseEntity<BasketItemResponse> setBasketItem(@RequestHeader String userUuid, @Valid @RequestBody BasketItemCreateRequest basketItemCreateRequest) {
        if (userUuid.isEmpty()) {
            throw new NoSuchElementException(messageUtil.getUserUuidEmptyMessage());
        }
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
    public ResponseEntity<BasketItemResponse> deleteBasketItem(@RequestHeader String userUuid, @RequestParam("itemUuid") String itemUuid) {
        basketService.deleteBasketItem(userUuid, itemUuid);
        return ResponseEntity.ok(BasketItemResponse.getInstance(itemUuid, messageUtil.getBasketItemDeleteMessage()));
    }

    @GetMapping("/list")
    @Operation(summary = "장바구니 상품 목록 조회", description = "장바구니에 등록한 상품 목록 전체를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                content = {@Content(schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "Fail")
    })
    public ResponseEntity<List<ItemSimpleWithCountResponse>> getBasketItemList(@RequestHeader String userUuid) {
        if (userUuid.isEmpty()) {
            throw new NoSuchElementException(messageUtil.getUserUuidEmptyMessage());
        }
        Member member = memberService.findMember(userUuid);
        Basket basket = basketService.findBasket(member);
        List<ItemSimpleWithCountResponse> basketItemSimpleWithCountResponseList = basketItemService.findAllByBasket(basket);
        return ResponseEntity.status(HttpStatus.OK).body(basketItemSimpleWithCountResponseList);
    }
}
