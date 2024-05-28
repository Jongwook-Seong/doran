package com.sjw.doran.memberservice.mapper;

import com.sjw.doran.memberservice.dto.MemberDto;
import com.sjw.doran.memberservice.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDto toMemberDto(Member member);
    List<MemberDto> toMemberDtoList(List<Member> memberList);
}
