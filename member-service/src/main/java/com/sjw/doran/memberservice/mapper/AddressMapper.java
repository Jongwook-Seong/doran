package com.sjw.doran.memberservice.mapper;

import com.sjw.doran.memberservice.entity.Address;
import com.sjw.doran.memberservice.kafka.member.MemberTopicMessage;
import com.sjw.doran.memberservice.mongodb.delivery.DeliveryDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "details", source = "address.details")
    @Mapping(target = "zipcode", source = "address.zipcode")
    MemberTopicMessage.AddressData toAddressData(Address address);

    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "details", source = "address.details")
    @Mapping(target = "zipcode", source = "address.zipcode")
    com.sjw.doran.memberservice.vo.response.order.Address toAddress(DeliveryDocument.AddressData address);
}
