package com.sjw.doran.itemservice.mapper;

import com.sjw.doran.itemservice.dto.ItemDto;
import com.sjw.doran.itemservice.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { BookMapper.class })
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDto toItemDto(Item item);
}
