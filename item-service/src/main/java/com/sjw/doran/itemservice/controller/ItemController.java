package com.sjw.doran.itemservice.controller;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.dto.ItemDto;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.service.ItemService;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;
    private final ModelMapper modelMapper;

    // 임시
    @GetMapping("/book/detail")
    public ResponseEntity<Item> bookDetail(@RequestParam String itemUuid) {
        Item itemDetail = itemService.getItemDetail(itemUuid);
        return new ResponseEntity<>(itemDetail, HttpStatus.OK);
    }

    @PostMapping("/book/register")
    public ResponseEntity<Void> registerBook(@RequestBody BookCreateRequest bookCreateRequest) {
        BookDto bookDto = BookDto.getInstanceForCreate(bookCreateRequest);
        itemService.saveBook(bookDto);
        return ResponseEntity.accepted().build();
    }
}
