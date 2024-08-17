package com.sjw.doran.memberservice.mapper;

import com.sjw.doran.memberservice.dto.MemberDto;
import com.sjw.doran.memberservice.entity.Address;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.kafka.common.OperationType;
import com.sjw.doran.memberservice.kafka.member.MemberTopicMessage;
import com.sjw.doran.memberservice.mongodb.member.MemberDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = { AddressMapper.class })
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDto toMemberDto(Member member);
    List<MemberDto> toMemberDtoList(List<Member> memberList);

    @Mappings({
            @Mapping(target = "id", source = "member.id"),
            @Mapping(target = "operationType", source = "operationType"),
            @Mapping(target = "payload.id", source = "member.id"),
            @Mapping(target = "payload.userUuid", source = "member.userUuid"),
            @Mapping(target = "payload.nickname", source = "member.nickname"),
            @Mapping(target = "payload.profileImageUrl", source = "member.profileImageUrl"),
            @Mapping(target = "payload.address", source = "address")
    })
    MemberTopicMessage toMemberTopicMessage(Member member, Address address, OperationType operationType);

    @Mappings({
            @Mapping(target = "id", source = "payload.id"),
            @Mapping(target = "userUuid", source = "payload.userUuid"),
            @Mapping(target = "nickname", source = "payload.nickname"),
            @Mapping(target = "profileImageUrl", source = "payload.profileImageUrl"),
            @Mapping(target = "address", source = "payload.address")
    })
    MemberDocument toMemberDocument(MemberTopicMessage.Payload payload);

    @Mapping(target = "city", source = "addressData.city")
    @Mapping(target = "street", source = "addressData.street")
    @Mapping(target = "details", source = "addressData.details")
    @Mapping(target = "zipcode", source = "addressData.zipcode")
    Address toAddress(MemberTopicMessage.AddressData addressData);

    @Mapping(target = "userUuid", source = "memberDocument.userUuid")
    @Mapping(target = "nickname", source = "memberDocument.nickname")
    @Mapping(target = "profileImageUrl", source = "memberDocument.profileImageUrl")
    MemberDto toMemberDto(MemberDocument memberDocument);

    List<MemberDto> toMemberDtoListFromMemberDocumentList(List<MemberDocument> memberDocumentList);
}
