package com.sjw.doran.requestdatagenerator.member.repository;

import com.sjw.doran.requestdatagenerator.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Transactional(value = "memberTransactionManager", readOnly = true)
    @Query(value = "SELECT m.user_uuid FROM Member m ORDER BY RAND() LIMIT 1", nativeQuery = true)
    String findAnyUserUuid();

    @Transactional(value = "memberTransactionManager", readOnly = true)
    @Query("SELECT m.userUuid FROM Member m")
    List<String> findAllUserUuid();
}
