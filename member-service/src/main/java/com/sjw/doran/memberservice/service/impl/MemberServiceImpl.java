package com.sjw.doran.memberservice.service.impl;

import com.sjw.doran.memberservice.dto.MemberDto;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.repository.MemberRepository;
import com.sjw.doran.memberservice.service.MemberService;
import com.sjw.doran.memberservice.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapperUtil modelMapperUtil;

    @Override
    public MemberDto findMember(String userUuid) {
        Optional<Member> member = memberRepository.findByUserUuid(userUuid);
        MemberDto memberDto = modelMapperUtil.convertToMemberDto(member.get());
        return memberDto;
    }

    @Override
    public List<MemberDto> findMembers() {
        List<Member> members = memberRepository.findAll();
        List<MemberDto> memberDtos = modelMapperUtil.mapMemberEntityListToDtoList(members);
        return memberDtos;
    }

    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }
}
