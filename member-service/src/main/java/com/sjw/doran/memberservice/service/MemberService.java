package com.sjw.doran.memberservice.service;

import com.sjw.doran.memberservice.dto.MemberDto;
import com.sjw.doran.memberservice.entity.Member;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberService {

    @Transactional(readOnly = true)
    Member findMember(String userUuid);
    @Transactional(readOnly = true)
    MemberDto findMemberDto(String userUuid);
    @Transactional(readOnly = true)
    List<MemberDto> findMembers();
    @Transactional
    void saveMember(Member member);
    @Transactional
    void deleteMember(String userUuid);
}
