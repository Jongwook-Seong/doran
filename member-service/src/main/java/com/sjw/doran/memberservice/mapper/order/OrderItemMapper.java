package com.sjw.doran.memberservice.mapper.order;

import com.sjw.doran.memberservice.kafka.order.OrderTopicMessage;
import com.sjw.doran.memberservice.mongodb.item.ItemDocument;
import com.sjw.doran.memberservice.mongodb.order.OrderDocument;
import com.sjw.doran.memberservice.vo.response.order.OrderItemSimple;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(target = "itemUuid", source = "itemData.itemUuid")
    @Mapping(target = "orderPrice", source = "itemData.price")
    @Mapping(target = "count", source = "itemData.count")
    OrderDocument.OrderItem toOrderDocumentOrderItem(OrderTopicMessage.ItemData itemData);

    List<OrderDocument.OrderItem> toOrderDocumentOrderItemList(List<OrderTopicMessage.ItemData> itemDataList);

    @Mapping(target = "itemName", source = "itemDocument.itemName")
    @Mapping(target = "itemImageUrl", source = "itemDocument.itemImageUrl")
    @Mapping(target = "itemUuid", source = "orderItem.itemUuid")
    @Mapping(target = "price", source = "orderItem.orderPrice")
    @Mapping(target = "count", source = "orderItem.count")
    OrderItemSimple toOrderItemSimple(OrderDocument.OrderItem orderItem, ItemDocument itemDocument);

    List<OrderItemSimple> toOrderItemSimpleList(List<OrderDocument.OrderItem> orderItems, List<ItemDocument> itemDocuments);
}
