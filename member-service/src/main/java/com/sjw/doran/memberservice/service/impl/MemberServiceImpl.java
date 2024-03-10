package com.sjw.doran.memberservice.service.impl;

import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.repository.MemberRepository;
import com.sjw.doran.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findMember(String userUuid) {
        Optional<Member> member = memberRepository.findByUserUuid(userUuid);
        return member.get();
    }

    @Override
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }
}
