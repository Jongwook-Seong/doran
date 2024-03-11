package com.sjw.doran.memberservice.service;

import com.sjw.doran.memberservice.dto.MemberDto;
import com.sjw.doran.memberservice.entity.Member;

import java.util.List;

public interface MemberService {

    MemberDto findMember(String userUuid);
    List<MemberDto> findMembers();
    void saveMember(Member member);
}
