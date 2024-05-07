package com.sjw.doran.itemservice.controller;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.dto.ItemDto;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.service.ItemService;
import com.sjw.doran.itemservice.util.MessageUtil;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import com.sjw.doran.itemservice.vo.request.ItemListRequest;
import com.sjw.doran.itemservice.vo.response.ItemSimpleResponse;
import com.sjw.doran.itemservice.vo.response.ItemSimpleWithQuantityResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item-service")
public class ItemController {

    private final ItemService itemService;
    private final ModelMapper modelMapper;
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
}
