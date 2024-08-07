package com.sjw.doran.memberservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.repository.MemberRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sjw.doran.memberservice.entity.QMember.member;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Member> findByUserUuid(String userUuid) {

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.userUuid.eq(userUuid))
                .fetchOne();

        return Optional.of(findMember);
    }
}
