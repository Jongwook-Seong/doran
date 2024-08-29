package com.sjw.doran.memberservice.mapper.order;

import com.sjw.doran.memberservice.kafka.delivery.DeliveryTopicMessage;
import com.sjw.doran.memberservice.mapper.AddressMapper;
import com.sjw.doran.memberservice.mongodb.delivery.DeliveryDocument;
import com.sjw.doran.memberservice.vo.response.order.DeliveryTrackingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { DeliveryTrackingMapper.class, TransceiverInfoMapper.class, AddressMapper.class },componentModel = "spring")
public interface DeliveryMapper {

    DeliveryMapper INSTANCE = Mappers.getMapper(DeliveryMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "payload.id"),
            @Mapping(target = "deliveryTrackingInfoList", source = "payload.deliveryTrackings"),
            @Mapping(target = "transceiverInfoData", source = "payload.transceiverInfoData"),
            @Mapping(target = "address", source = "payload.address"),
            @Mapping(target = "deliveryStatus", source = "payload.deliveryStatus")
    })
    DeliveryDocument toDeliveryDocument(DeliveryTopicMessage.Payload payload);

    @Mapping(target = "deliveryTrackingInfoList", source = "deliveryDocument.deliveryTrackingInfoList")
    @Mapping(target = "deliveryStatus", source = "deliveryDocument.deliveryStatus")
    DeliveryTrackingResponse toDeliveryTrackingResponse(DeliveryDocument deliveryDocument);
}
