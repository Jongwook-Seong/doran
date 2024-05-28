package com.sjw.doran.orderservice.mapper;

import com.sjw.doran.orderservice.dto.DeliveryDto;
import com.sjw.doran.orderservice.dto.DeliveryTrackingDto;
import com.sjw.doran.orderservice.dto.OrderDto;
import com.sjw.doran.orderservice.dto.OrderItemDto;
import com.sjw.doran.orderservice.entity.Delivery;
import com.sjw.doran.orderservice.entity.DeliveryTracking;
import com.sjw.doran.orderservice.entity.Order;
import com.sjw.doran.orderservice.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toOrder(OrderDto orderDto);
    OrderItem toOrderItem(OrderItemDto orderItemDto);
    Delivery toDelivery(DeliveryDto deliveryDto);
    @Mapping(target = "delivery", source = "delivery")
    DeliveryTracking toDeliveryTracking(DeliveryTrackingDto deliveryTrackingDto, Delivery delivery);
}
