package com.sjw.doran.itemservice.mapper;

import com.sjw.doran.itemservice.dto.ItemDto;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.mongodb.item.ItemDocument;
import com.sjw.doran.itemservice.redis.data.BestItem;
import com.sjw.doran.itemservice.vo.response.ItemSimpleResponse;
import com.sjw.doran.itemservice.vo.response.ItemSimpleWithQuantityResponse;
import com.sjw.doran.itemservice.vo.response.ItemSimpleWithoutPriceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = { BookMapper.class, ArtworkMapper.class }, componentModel = "spring")
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDto toItemDto(Item item);
    ItemSimpleResponse toItemSimpleResponse(ItemDto itemDto);
    ItemSimpleResponse toItemSimpleResponse(ItemDocument itemDocument);
    List<ItemSimpleResponse> toItemSimpleResponseList(List<ItemDocument> itemDocumentList);
    @Mapping(target = "quantity", source = "itemDto.stockQuantity")
    ItemSimpleWithQuantityResponse toItemSimpleWQResponse(ItemDto itemDto);
    @Mapping(target = "quantity", source = "itemDocument.stockQuantity")
    ItemSimpleWithQuantityResponse toItemSimpleWQResponse(ItemDocument itemDocument);
    List<ItemSimpleWithQuantityResponse> toItemSimpleWQResponseList(List<ItemDocument> itemDocumentList);
    ItemSimpleWithoutPriceResponse toItemSimpleWxPResponse(ItemDto itemDto);
    ItemSimpleWithoutPriceResponse toItemSimpleWxPResponse(ItemDocument itemDocument);
    List<ItemSimpleWithoutPriceResponse> toItemSimpleWxPResponseList(List<ItemDocument> itemDocumentList);
    @Mappings({
            @Mapping(target = "bookInfo", source = "itemDocument.bookInfo"),
            @Mapping(target = "artworkInfo", source = "itemDocument.artworkInfo")
    })
    BestItem toBestItem(ItemDocument itemDocument);
    List<BestItem> toBestItemList(List<ItemDocument> itemDocumentList);
}
