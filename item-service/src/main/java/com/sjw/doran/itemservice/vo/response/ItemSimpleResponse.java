package com.sjw.doran.itemservice.vo.response;

import com.sjw.doran.itemservice.dto.BookDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemSimpleResponse {

    private String itemUuid;
    private String itemName;
    private int price;
    private String itemImageUrl;

    public static ItemSimpleResponse getInstanceAsBook(BookDto bookDto) {
        return ItemSimpleResponse.builder()
                .itemUuid(bookDto.getItemUuid())
                .itemName(bookDto.getItemName())
                .price(bookDto.getPrice())
                .itemImageUrl(bookDto.getItemImageUrl())
                .build();
    }
}
