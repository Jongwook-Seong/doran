package com.sjw.doran.itemservice.mapper;

import com.sjw.doran.itemservice.dto.ItemDto;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.vo.response.ItemSimpleResponse;
import com.sjw.doran.itemservice.vo.response.ItemSimpleWithQuantityResponse;
import com.sjw.doran.itemservice.vo.response.ItemSimpleWithoutPriceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { BookMapper.class }, componentModel = "spring")
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDto toItemDto(Item item);
    ItemSimpleResponse toItemSimpleResponse(ItemDto itemDto);
    ItemSimpleWithQuantityResponse toItemSimpleWQResponse(ItemDto itemDto);
    ItemSimpleWithoutPriceResponse toItemSimpleWxPResponse(ItemDto itemDto);
}
