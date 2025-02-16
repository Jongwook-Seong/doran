package com.sjw.doran.memberservice.testcontainers.connection;

import com.sjw.doran.memberservice.repository.MemberRepository;
import com.sjw.doran.memberservice.testcontainers.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
public class ConnectionTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testMemberRepository() {
        assertThat(memberRepository.count()).isGreaterThan(0);
    }
}
