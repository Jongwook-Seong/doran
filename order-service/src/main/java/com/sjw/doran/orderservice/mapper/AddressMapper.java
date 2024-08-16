package com.sjw.doran.orderservice.mapper;

import com.sjw.doran.orderservice.entity.Address;
import com.sjw.doran.orderservice.kafka.delivery.DeliveryTopicMessage;
import com.sjw.doran.orderservice.mongodb.delivery.DeliveryDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "details", source = "address.details")
    @Mapping(target = "zipcode", source = "address.zipcode")
    DeliveryTopicMessage.AddressData toAddressData(Address address);

    @Mapping(target = "city", source = "addressData.city")
    @Mapping(target = "street", source = "addressData.street")
    @Mapping(target = "details", source = "addressData.details")
    @Mapping(target = "zipcode", source = "addressData.zipcode")
    DeliveryDocument.Address toDeliveryDocumentAddress(DeliveryTopicMessage.AddressData addressData);

    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "details", source = "address.details")
    @Mapping(target = "zipcode", source = "address.zipcode")
    Address toAddress(DeliveryDocument.Address address);
}
