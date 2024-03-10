package com.sjw.doran.memberservice.repository;

import com.sjw.doran.memberservice.entity.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<Member> findByUserUuid(String userUuid);
}
