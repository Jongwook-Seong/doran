package com.sjw.doran.itemservice.dto;

import com.sjw.doran.itemservice.entity.Category;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@RequiredArgsConstructor
public class ItemDto {

    private String itemUuid;
    private String itemName;
    private int price;
    private int stockQuantity;
    private String itemImageUrl;
    private Category category;
}
