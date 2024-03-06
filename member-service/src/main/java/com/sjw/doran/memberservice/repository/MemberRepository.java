package com.sjw.doran.memberservice.repository;

import com.sjw.doran.memberservice.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long>, MemberRepositoryCustom {
}
