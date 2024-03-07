package com.sjw.doran.memberservice.repository;

import com.sjw.doran.memberservice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
}
