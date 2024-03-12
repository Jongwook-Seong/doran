package com.sjw.doran.memberservice.repository;

import com.sjw.doran.memberservice.entity.Member;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepositoryCustom {

    @Transactional(readOnly = true)
    Optional<Member> findByUserUuid(String userUuid);
}
