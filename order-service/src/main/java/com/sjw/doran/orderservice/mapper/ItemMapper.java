package com.sjw.doran.orderservice.mapper;

import com.sjw.doran.orderservice.kafka.item.ItemTopicMessage;
import com.sjw.doran.orderservice.mongodb.item.ItemDocument;
import com.sjw.doran.orderservice.vo.response.ItemSimpleWithoutPriceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "itemUuid", source = "itemUuid")
    @Mapping(target = "itemName", source = "itemName")
    @Mapping(target = "itemImageUrl", source = "itemImageUrl")
    ItemDocument toItemDocument(ItemTopicMessage.Payload payload);

    @Mapping(target = "itemUuid", source = "itemUuid")
    @Mapping(target = "itemName", source = "itemName")
    @Mapping(target = "itemImageUrl", source = "itemImageUrl")
    ItemSimpleWithoutPriceResponse toItemSimpleWxPResponse(ItemDocument itemDocument);

    List<ItemSimpleWithoutPriceResponse> toItemSimpleWxPResponseList(List<ItemDocument> itemDocuments);
}
