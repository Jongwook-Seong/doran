package com.sjw.doran.orderservice.mapper;

import com.sjw.doran.orderservice.entity.TransceiverInfo;
import com.sjw.doran.orderservice.kafka.delivery.DeliveryTopicMessage;
import com.sjw.doran.orderservice.mongodb.delivery.DeliveryDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransceiverInfoMapper {

    @Mapping(target = "ordererName", source = "transceiverInfo.ordererName")
    @Mapping(target = "receiverName", source = "transceiverInfo.receiverName")
    @Mapping(target = "receiverPhoneNumber", source = "transceiverInfo.receiverPhoneNumber")
    DeliveryTopicMessage.TransceiverInfoData toTransceiverInfoData(TransceiverInfo transceiverInfo);

    @Mapping(target = "ordererName", source = "transceiverInfoData.ordererName")
    @Mapping(target = "receiverName", source = "transceiverInfoData.receiverName")
    @Mapping(target = "receiverPhoneNumber", source = "transceiverInfoData.receiverPhoneNumber")
    DeliveryDocument.TransceiverInfo toDeliveryDocumentTransceiverInfo(DeliveryTopicMessage.TransceiverInfoData transceiverInfoData);

    @Mapping(target = "ordererName", source = "transceiverInfo.ordererName")
    @Mapping(target = "receiverName", source = "transceiverInfo.receiverName")
    @Mapping(target = "receiverPhoneNumber", source = "transceiverInfo.receiverPhoneNumber")
    TransceiverInfo toTransceiverInfo(DeliveryDocument.TransceiverInfo transceiverInfo);
}
