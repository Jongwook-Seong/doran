package com.sjw.doran.memberservice.mapper;

import com.sjw.doran.memberservice.kafka.item.ItemTopicMessage;
import com.sjw.doran.memberservice.mongodb.item.ItemDocument;
import com.sjw.doran.memberservice.vo.response.item.ItemSimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "itemUuid", source = "payload.itemUuid")
    @Mapping(target = "itemName", source = "payload.itemName")
    @Mapping(target = "itemImageUrl", source = "payload.itemImageUrl")
    @Mapping(target = "category", source = "payload.category")
    @Mapping(target = "price", source = "payload.price")
    ItemDocument toItemDocument(ItemTopicMessage.Payload payload);

    @Mapping(target = "itemUuid", source = "itemDocument.itemUuid")
    @Mapping(target = "itemName", source = "itemDocument.itemName")
    @Mapping(target = "itemImageUrl", source = "itemDocument.itemImageUrl")
    @Mapping(target = "category", source = "itemDocument.category")
    @Mapping(target = "price", source = "itemDocument.price")
    ItemSimpleResponse toItemSimpleResponse(ItemDocument itemDocument);

    List<ItemSimpleResponse> toItemSimpleResponseList(List<ItemDocument> itemDocuments);
}
