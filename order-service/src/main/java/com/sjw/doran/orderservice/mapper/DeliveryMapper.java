package com.sjw.doran.orderservice.mapper;

import com.sjw.doran.orderservice.dto.DeliveryDto;
import com.sjw.doran.orderservice.dto.DeliveryTrackingDto;
import com.sjw.doran.orderservice.entity.Delivery;
import com.sjw.doran.orderservice.entity.DeliveryTracking;
import com.sjw.doran.orderservice.kafka.common.OperationType;
import com.sjw.doran.orderservice.kafka.delivery.DeliveryTopicMessage;
import com.sjw.doran.orderservice.mongodb.delivery.DeliveryDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = { TransceiverInfoMapper.class, AddressMapper.class, DeliveryTrackingMapper.class })
public interface DeliveryMapper {

    DeliveryMapper INSTANCE = Mappers.getMapper(DeliveryMapper.class);

    Delivery toDelivery(DeliveryDto deliveryDto);
    @Mapping(target = "delivery", source = "delivery")
    DeliveryTracking toDeliveryTracking(DeliveryTrackingDto deliveryTrackingDto, Delivery delivery);

    @Mappings({
            @Mapping(target = "id", source = "delivery.id"),
            @Mapping(target = "operationType", source = "operationType"),
            @Mapping(target = "payload.id", source = "delivery.id"),
            @Mapping(target = "payload.deliveryStatus", source = "delivery.deliveryStatus"),
            @Mapping(target = "payload.transceiverInfo", source = "delivery.transceiverInfo"),
            @Mapping(target = "payload.address", source = "delivery.address"),
            @Mapping(target = "payload.deliveryTrackings", source = "deliveryTrackings")
    })
    DeliveryTopicMessage toDeliveryTopicMessage(Delivery delivery, List<DeliveryTracking> deliveryTrackings, OperationType operationType);

    @Mappings({
            @Mapping(target = "id", source = "payload.id"),
            @Mapping(target = "deliveryStatus", source = "payload.deliveryStatus"),
            @Mapping(target = "transceiverInfo", source = "payload.transceiverInfo"),
            @Mapping(target = "address", source = "payload.address"),
            @Mapping(target = "deliveryTrackings", source = "payload.deliveryTrackings")
    })
    DeliveryDocument toDeliveryDocument(DeliveryTopicMessage.Payload payload);
}
