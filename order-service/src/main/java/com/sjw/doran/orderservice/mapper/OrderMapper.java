package com.sjw.doran.orderservice.mapper;

import com.sjw.doran.orderservice.dto.OrderDto;
import com.sjw.doran.orderservice.dto.OrderItemDto;
import com.sjw.doran.orderservice.entity.Order;
import com.sjw.doran.orderservice.entity.OrderItem;
import com.sjw.doran.orderservice.kafka.common.OperationType;
import com.sjw.doran.orderservice.kafka.order.OrderTopicMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = { OrderItemMapper.class })
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toOrder(OrderDto orderDto);
    OrderItem toOrderItem(OrderItemDto orderItemDto);

    @Mappings({
            @Mapping(target = "id", source = "order.id"),
            @Mapping(target = "operationType", source = "operationType"),
            @Mapping(target = "payload.id", source = "order.id"),
            @Mapping(target = "payload.orderUuid", source = "order.orderUuid"),
            @Mapping(target = "payload.userUuid", source = "order.userUuid"),
            @Mapping(target = "payload.items", source = "orderItems"),
            @Mapping(target = "payload.deliveryId", source = "deliveryId"),
            @Mapping(target = "payload.orderStatus", source = "order.orderStatus"),
            @Mapping(target = "payload.orderDate", source = "order.orderDate")
    })
    OrderTopicMessage toOrderTopicMessage(Order order, List<OrderItem> orderItems, Long deliveryId, OperationType operationType);
}
