package com.sjw.doran.memberservice.util;

import com.sjw.doran.memberservice.dto.BasketItemDto;
import com.sjw.doran.memberservice.dto.MemberDto;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.entity.Member;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ModelMapperUtil {

    private final ModelMapper mapper;

    public MemberDto convertToMemberDto(Member member) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        MemberDto memberDto = mapper.map(member, MemberDto.class);
        return memberDto;
    }

    public Member convertToMember(MemberDto memberDto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Member member = mapper.map(memberDto, Member.class);
        return member;
    }

    public List<MemberDto> mapMemberEntityListToDtoList(List<Member> members) {
        List<MemberDto> memberDtos = members.stream()
                .map(this::convertToMemberDto)
                .collect(Collectors.toList());
        return memberDtos;
    }

    public BasketItem BasketItemDtoConvertToEntity(BasketItemDto basketItemDto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        BasketItem basketItem = mapper.map(basketItemDto, BasketItem.class);
        return basketItem;
    }
}
