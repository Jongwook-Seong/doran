package com.sjw.doran.memberservice.mapper;

import com.sjw.doran.memberservice.entity.Address;
import com.sjw.doran.memberservice.kafka.member.MemberTopicMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "details", source = "address.details")
    @Mapping(target = "zipcode", source = "address.zipcode")
    MemberTopicMessage.AddressData toAddressData(Address address);
}
