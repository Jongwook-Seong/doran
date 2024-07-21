package com.sjw.doran.memberservice.mapper;

import com.sjw.doran.memberservice.dto.MemberDto;
import com.sjw.doran.memberservice.entity.Address;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.kafka.common.OperationType;
import com.sjw.doran.memberservice.kafka.member.MemberTopicMessage;
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
}
