package com.sjw.doran.memberservice.mapper.order;

import com.sjw.doran.memberservice.kafka.delivery.DeliveryTopicMessage;
import com.sjw.doran.memberservice.mongodb.delivery.DeliveryDocument;
import com.sjw.doran.memberservice.vo.response.order.DeliveryTrackingInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryTrackingMapper {

    DeliveryTrackingMapper INSTANCE = Mappers.getMapper(DeliveryTrackingMapper.class);

    @Mapping(target = "courier", source = "deliveryTrackingData.courier")
    @Mapping(target = "contactNumber", source = "deliveryTrackingData.contactNumber")
    @Mapping(target = "postLocation", source = "deliveryTrackingData.postLocation")
    @Mapping(target = "postDateTime", source = "deliveryTrackingData.postDateTime")
    DeliveryDocument.DeliveryTrackingInfo toDeliveryDocumentDeliveryTrackingInfo(DeliveryTopicMessage.DeliveryTrackingData deliveryTrackingData);

    List<DeliveryDocument.DeliveryTrackingInfo> toDeliveryDocumentDeliveryTrackingInfoList(List<DeliveryTopicMessage.DeliveryTrackingData> deliveryTrackingDataList);
}
