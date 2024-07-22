package com.sjw.doran.itemservice.vo.response;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.dto.ItemDto;
import com.sjw.doran.itemservice.entity.Category;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSimpleResponse {

    private String itemUuid;
    private String itemName;
    private int price;
    private String itemImageUrl;
    private Category category;

    public static ItemSimpleResponse getInstanceAsItem(ItemDto itemDto) {
        return ItemSimpleResponse.builder()
                .itemUuid(itemDto.getItemUuid())
                .itemName(itemDto.getItemName())
                .price(itemDto.getPrice())
                .itemImageUrl(itemDto.getItemImageUrl())
                .category(itemDto.getCategory())
                .build();
    }

    public static ItemSimpleResponse getInstanceAsBook(BookDto bookDto) {
        return ItemSimpleResponse.builder()
                .itemUuid(bookDto.getItemUuid())
                .itemName(bookDto.getItemName())
                .price(bookDto.getPrice())
                .itemImageUrl(bookDto.getItemImageUrl())
                .category(Category.BOOK)
                .build();
    }
}
