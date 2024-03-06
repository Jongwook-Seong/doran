package com.sjw.doran.memberservice.service;

import com.sjw.doran.memberservice.entity.MemberEntity;

import java.util.List;

public interface MemberService {

    List<MemberEntity> findMembers();
    void saveMember(MemberEntity member);
}
