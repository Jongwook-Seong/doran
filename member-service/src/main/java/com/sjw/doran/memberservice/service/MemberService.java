package com.sjw.doran.memberservice.service;

import com.sjw.doran.memberservice.entity.Member;

import java.util.List;

public interface MemberService {

    Member findMember(String userUuid);
    List<Member> findMembers();
    void saveMember(Member member);
}
