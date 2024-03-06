package com.sjw.doran.memberservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.memberservice.entity.MemberEntity;
import com.sjw.doran.memberservice.entity.QMemberEntity;
import com.sjw.doran.memberservice.repository.MemberRepositoryCustom;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.sjw.doran.memberservice.entity.QMemberEntity.memberEntity;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<MemberEntity> findByMemberUuid(String memberUuid) {

        MemberEntity member = queryFactory
                .selectFrom(memberEntity)
                .where(memberEntity.memberUuid.eq(memberUuid))
                .fetchOne();

        return Optional.of(member);
    }
}
