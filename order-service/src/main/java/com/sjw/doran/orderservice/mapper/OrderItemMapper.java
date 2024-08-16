package com.sjw.doran.orderservice.mapper;

import com.sjw.doran.orderservice.entity.OrderItem;
import com.sjw.doran.orderservice.kafka.order.OrderTopicMessage;
import com.sjw.doran.orderservice.mongodb.order.OrderDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "itemUuid", source = "orderItem.itemUuid")
    @Mapping(target = "price", source = "orderItem.orderPrice")
    @Mapping(target = "count", source = "orderItem.count")
    OrderTopicMessage.ItemData toItemData(OrderItem orderItem);

    List<OrderTopicMessage.ItemData> toItemDataList(List<OrderItem> orderItems);

    @Mapping(target = "itemUuid", source = "itemData.itemUuid")
    @Mapping(target = "orderPrice", source = "itemData.price")
    @Mapping(target = "count", source = "itemData.count")
    OrderDocument.OrderItem toOrderDocumentOrderItem(OrderTopicMessage.ItemData itemData);

    List<OrderDocument.OrderItem> toOrderDocumentOrderItemList(List<OrderTopicMessage.ItemData> itemData);
}
