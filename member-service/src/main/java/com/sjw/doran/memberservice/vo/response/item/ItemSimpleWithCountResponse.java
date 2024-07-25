package com.sjw.doran.memberservice.vo.response.item;

import com.sjw.doran.memberservice.entity.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemSimpleWithCountResponse {

    private String itemUuid;
    private String itemName;
    private String itemImageUrl;
    private Category category;
    private int price;
    private int totalPrice;
    private int count;

    public static ItemSimpleWithCountResponse getInstance(ItemSimpleResponse itemSimpleResponse, int count) {
        return ItemSimpleWithCountResponse.builder()
                .itemUuid(itemSimpleResponse.getItemUuid())
                .itemName(itemSimpleResponse.getItemName())
                .itemImageUrl(itemSimpleResponse.getItemImageUrl())
                .category(itemSimpleResponse.getCategory())
                .price(itemSimpleResponse.getPrice())
                .totalPrice(itemSimpleResponse.getPrice() * count)
                .count(count)
                .build();
    }
}
