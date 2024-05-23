package com.sjw.doran.itemservice.mapper;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.dto.ItemDto;
import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.entity.Category;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;

import java.util.UUID;

public class ItemMapperImpl implements ItemMapper {

    @Override
    public ItemDto toItemDto(Item item) {

        if (item == null) {
            return null;
        }

        return ItemDto.builder()
                .itemUuid( UUID.randomUUID().toString() )
                .itemName( item.getItemName() )
                .price( item.getPrice() )
                .stockQuantity( item.getStockQuantity() )
                .itemImageUrl( item.getItemImageUrl() )
                .category( item.getCategory() )
                .build();
    }
}
