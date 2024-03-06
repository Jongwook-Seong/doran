package com.sjw.doran.memberservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.memberservice.entity.MemberEntity;
import com.sjw.doran.memberservice.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class MemberRepositoryImplTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void findByMemberUuid() {
        MemberEntity member = new MemberEntity("111", "nick", "url");
        memberRepository.save(member);

        Optional<MemberEntity> findMemberUuid = memberRepository.findByMemberUuid("111");

        Assertions.assertThat(findMemberUuid.get()).isEqualTo(member);
        log.info("member : " + member);
        log.info("byMemberUuid : " + findMemberUuid.get());
    }
}