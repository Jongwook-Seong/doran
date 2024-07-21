package com.sjw.doran.orderservice.mapper;

import com.sjw.doran.orderservice.entity.OrderItem;
import com.sjw.doran.orderservice.kafka.order.OrderTopicMessage;
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
}
