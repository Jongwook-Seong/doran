package com.sjw.doran.memberservice.mapper.order;

import com.sjw.doran.memberservice.mongodb.delivery.DeliveryDocument;
import com.sjw.doran.memberservice.vo.response.order.TransceiverInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransceiverInfoMapper {

    TransceiverInfoMapper INSTANCE = Mappers.getMapper(TransceiverInfoMapper.class);

    @Mapping(target = "ordererName", source = "transceiverInfoData.ordererName")
    @Mapping(target = "receiverName", source = "transceiverInfoData.receiverName")
    @Mapping(target = "receiverPhoneNumber", source = "transceiverInfoData.receiverPhoneNumber")
    TransceiverInfo toTransceiverInfo(DeliveryDocument.TransceiverInfoData transceiverInfoData);
}
