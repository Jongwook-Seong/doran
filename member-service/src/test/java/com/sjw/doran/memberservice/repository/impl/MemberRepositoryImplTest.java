package com.sjw.doran.memberservice.repository.impl;

import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@SpringBootTest
@Transactional
class MemberRepositoryImplTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void findByMemberUuid() {
        Member member = new Member("111", "nick", "url");
        memberRepository.save(member);

        Optional<Member> findMemberUuid = memberRepository.findByMemberUuid("111");

        Assertions.assertThat(findMemberUuid.get()).isEqualTo(member);
        log.info("member : " + member);
        log.info("byMemberUuid : " + findMemberUuid.get());
    }
}