package com.sjw.doran.orderservice.mapper;

import com.sjw.doran.orderservice.entity.Delivery;
import com.sjw.doran.orderservice.entity.DeliveryTracking;
import com.sjw.doran.orderservice.kafka.delivery.DeliveryTopicMessage;
import com.sjw.doran.orderservice.mongodb.DeliveryDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryTrackingMapper {

    @Mapping(target = "courier", source = "deliveryTracking.courier")
    @Mapping(target = "contactNumber", source = "deliveryTracking.contactNumber")
    @Mapping(target = "postLocation", source = "deliveryTracking.postLocation")
    @Mapping(target = "postDateTime", source = "deliveryTracking.postDateTime")
    DeliveryTopicMessage.DeliveryTrackingData toDeliveryTrackingData(DeliveryTracking deliveryTracking);

    List<DeliveryTopicMessage.DeliveryTrackingData> toDeliveryTrackingDateList(List<DeliveryTracking> deliveryTrackings);

    @Mapping(target = "courier", source = "deliveryTrackingData.courier")
    @Mapping(target = "contactNumber", source = "deliveryTrackingData.contactNumber")
    @Mapping(target = "postLocation", source = "deliveryTrackingData.postLocation")
    @Mapping(target = "postDateTime", source = "deliveryTrackingData.postDateTime")
    DeliveryDocument.DeliveryTracking toDeliveryDocumentDeliveryTracking(DeliveryTopicMessage.DeliveryTrackingData deliveryTrackingData);

    List<DeliveryDocument.DeliveryTracking> toDeliveryDocumentDeliveryTrackingList(List<DeliveryTopicMessage.DeliveryTrackingData> deliveryTrackingDataList);

    @Mapping(target = "courier", source = "deliveryTracking.courier")
    @Mapping(target = "contactNumber", source = "deliveryTracking.contactNumber")
    @Mapping(target = "postLocation", source = "deliveryTracking.postLocation")
//    @Mapping(target = "postDateTime", source = "deliveryTracking.postDateTime")
    DeliveryTracking toDeliveryTracking(DeliveryDocument.DeliveryTracking deliveryTracking);

    List<DeliveryTracking> toDeliveryTrackingList(List<DeliveryDocument.DeliveryTracking> deliveryTrackings);
}
