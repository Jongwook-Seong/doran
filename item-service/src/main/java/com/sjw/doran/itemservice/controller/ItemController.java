package com.sjw.doran.itemservice.controller;

import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.service.ItemService;
import com.sjw.doran.itemservice.util.MessageUtil;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import com.sjw.doran.itemservice.vo.response.ItemSimpleResponse;
import com.sjw.doran.itemservice.vo.response.ItemSimpleWithQuantityResponse;
import com.sjw.doran.itemservice.vo.response.ItemSimpleWithoutPriceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final MessageUtil messageUtil;

    // 임시
    /** 책 상세 보기 **/
    @GetMapping("/book/detail")
    public ResponseEntity<Item> bookDetail(@RequestParam String itemUuid) {
        if (itemUuid.isEmpty()) {
            throw new NoSuchElementException(messageUtil.getItemUuidEmptyErrorMessage());
        }
        Item itemDetail = itemService.getItemDetail(itemUuid);
        return new ResponseEntity<>(itemDetail, HttpStatus.OK);
    }

    /** 책 장바구니 목록 조회하기 **/
    @GetMapping("/book/basket")
    public ResponseEntity<List<ItemSimpleResponse>> getBookBasket(@RequestParam("itemUuidList") List<String> itemUuidList) {
        List<ItemSimpleResponse> itemSimpleResponseList = itemService.getItemSimpleList(itemUuidList);
        return new ResponseEntity<>(itemSimpleResponseList, HttpStatus.OK);
    }

    /** 아이템 주문 목록 조회하기 **/
    @GetMapping("/orderitems")
    public ResponseEntity<List<ItemSimpleWithQuantityResponse>> getOrderItems(@RequestParam("itemUuidList") List<String> itemUuidList) {
        List<ItemSimpleWithQuantityResponse> itemSimpleWQResponseList = itemService.getItemSimpleWithQuantityList(itemUuidList);
        return new ResponseEntity<>(itemSimpleWQResponseList, HttpStatus.OK);
    }

    /** 아이템 구매하기 **/
    @PutMapping("/orderitems")
    public ResponseEntity<Void> orderItems(@RequestParam("itemUuidList") List<String> itemUuidList, @RequestParam("countList") List<Integer> countList) {
        itemService.subtractItems(itemUuidList, countList);
        return ResponseEntity.accepted().build();
    }

    /** 아이템 구매 취소하기 **/
    @PutMapping("/orderitems/cancel")
    public ResponseEntity<Void> cancelOrderItems(@RequestParam("itemUuidList") List<String> itemUuidList, @RequestParam("countList") List<Integer> countList) {
        itemService.restoreItems(itemUuidList, countList);
        return ResponseEntity.accepted().build();
    }

    // 임시
    @GetMapping("/book/search")
    public ResponseEntity<List<ItemSimpleResponse>> bookSearch(@RequestParam String keywords) {
        List<ItemSimpleResponse> itemSimpleResponseList = itemService.getBooksByKeyword(keywords);
//        List<ItemSimpleResponse> itemSimpleResponseList = itemService.getItemsByKeyword(keywords);
        return new ResponseEntity<>(itemSimpleResponseList, HttpStatus.OK);
    }

    /** 책 등록하기 **/
    @PostMapping("/book/register")
    public ResponseEntity<Void> registerBook(@Valid @RequestBody BookCreateRequest bookCreateRequest) {
        itemService.saveBook(bookCreateRequest);
        return ResponseEntity.accepted().build();
    }

    /** 책 삭제하기 **/
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteItem(@RequestParam String itemUuid) {
        if (itemUuid.isEmpty()) {
            throw new NoSuchElementException(messageUtil.getItemUuidEmptyErrorMessage());
        }
        itemService.deleteItem(itemUuid);
        return ResponseEntity.accepted().build();
    }

    /** 아이템 단순정보(itemUuid, itemName, itemImageUrl) 조회(서비스간 통신 호출) **/
    @GetMapping("/items/simple")
    public ResponseEntity<List<ItemSimpleWithoutPriceResponse>> getItemSimpleWithoutPrice(@RequestParam("itemUuidList") List<String> itemUuidList) {
        if (itemUuidList.isEmpty()) {
            throw new NoSuchElementException(messageUtil.getItemUuidEmptyErrorMessage());
        }
        List<ItemSimpleWithoutPriceResponse> itemSimpleWithoutPriceList = itemService.getItemSimpleWithoutPriceList(itemUuidList);
        return new ResponseEntity<>(itemSimpleWithoutPriceList, HttpStatus.OK);
    }
}
