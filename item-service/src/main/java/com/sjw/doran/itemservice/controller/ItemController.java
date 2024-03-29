package com.sjw.doran.itemservice.controller;

import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;

    // 임시
    @GetMapping("/book/detail")
    public ResponseEntity<Item> bookDetail(@RequestParam String itemUuid) {
        Item itemDetail = itemService.getItemDetail(itemUuid);
        return new ResponseEntity<>(itemDetail, HttpStatus.OK);
    }

    @PostMapping("/item/new")
    public ResponseEntity<Void> newItem(@RequestBody Item item) {
        itemService.saveItem(item);
        return ResponseEntity.accepted().build();
    }
}
