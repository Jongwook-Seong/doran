package com.sjw.doran.memberservice.repository;

import com.sjw.doran.memberservice.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<MemberEntity> findByMemberUuid(String memberUuid);
}
