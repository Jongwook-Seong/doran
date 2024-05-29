package com.sjw.doran.memberservice.service.impl;

import com.sjw.doran.memberservice.dto.MemberDto;
import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.mapper.MemberMapper;
import com.sjw.doran.memberservice.repository.BasketRepository;
import com.sjw.doran.memberservice.repository.MemberRepository;
import com.sjw.doran.memberservice.service.BasketService;
import com.sjw.doran.memberservice.service.MemberService;
import com.sjw.doran.memberservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BasketRepository basketRepository;
    private final MemberMapper memberMapper;
    private final MessageUtil messageUtil;

    @Override
    @Transactional(readOnly = true)
    public Member findMember(String userUuid) {
        Optional<Member> member = memberRepository.findByUserUuid(userUuid);
        return member.get();
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDto findMemberDto(String userUuid) {
        Optional<Member> member = memberRepository.findByUserUuid(userUuid);
        MemberDto memberDto = memberMapper.toMemberDto(member.get());
        return memberDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberDto> findMembers() {
        List<Member> members = memberRepository.findAll();
        List<MemberDto> memberDtos = memberMapper.toMemberDtoList(members);
        return memberDtos;
    }

    @Override
    @Transactional
    public void saveMember(Member member) {
        try {
            memberRepository.save(member);
            Basket basket = new Basket(member);
            try {
                basketRepository.save(basket);
            } catch (Exception e) {
                throw new RuntimeException(messageUtil.getBasketCreateErrorMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(messageUtil.getMemberCreateErrorMessage());
        }
    }

    @Override
    @Transactional
    public void deleteMember(String userUuid) {
        try {
            Optional<Member> member = memberRepository.findByUserUuid(userUuid);
            memberRepository.delete(member.get());
        } catch (Exception e) {
            throw new RuntimeException(messageUtil.getMemberDeleteErrorMessage());
        }
    }
}
