package com.sjw.doran.memberservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.entity.QMember;
import com.sjw.doran.memberservice.repository.MemberRepositoryCustom;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.sjw.doran.memberservice.entity.QMember.member;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Member> findByMemberUuid(String memberUuid) {

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.memberUuid.eq(memberUuid))
                .fetchOne();

        return Optional.of(findMember);
    }
}
