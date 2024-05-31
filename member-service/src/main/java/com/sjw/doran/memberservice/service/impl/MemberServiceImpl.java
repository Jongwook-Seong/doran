package com.sjw.doran.memberservice.service.impl;

import com.sjw.doran.memberservice.client.OrderServiceClient;
import com.sjw.doran.memberservice.dto.MemberDto;
import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.mapper.MemberMapper;
import com.sjw.doran.memberservice.repository.BasketRepository;
import com.sjw.doran.memberservice.repository.MemberRepository;
import com.sjw.doran.memberservice.service.MemberService;
import com.sjw.doran.memberservice.util.MessageUtil;
import com.sjw.doran.memberservice.vo.response.MemberOrderResponse;
import com.sjw.doran.memberservice.vo.response.order.DeliveryTrackingResponse;
import com.sjw.doran.memberservice.vo.response.order.OrderDetailResponse;
import com.sjw.doran.memberservice.vo.response.order.OrderListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BasketRepository basketRepository;
    private final OrderServiceClient orderServiceClient;
    private final MemberMapper memberMapper;
    private final MessageUtil messageUtil;
    private final CircuitBreakerFactory circuitBreakerFactory;

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

    @Override
    @Transactional(readOnly = true)
    public MemberOrderResponse findMemberOrderList(String userUuid) {
        Member member = memberRepository.findByUserUuid(userUuid).orElseThrow(() -> {
            throw new NoSuchElementException("Invalid Member"); });
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("MS-findMemberOrderList-circuitebreaker");
        OrderListResponse orderListResponse = circuitBreaker.run(() ->
                orderServiceClient.inquireOrderList(userUuid), throwable -> null);
        return MemberOrderResponse.getInstance(member.getUserUuid(), member.getNickname(), member.getProfileImageUrl(), orderListResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberOrderResponse findMemberOrderDetail(String userUuid, String orderUuid) {
        Member member = memberRepository.findByUserUuid(userUuid).orElseThrow(() -> {
            throw new NoSuchElementException("Invalid Member"); });
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("MS-findMemberOrderDetail-circuitbreaker");
        OrderDetailResponse orderDetailResponse = circuitBreaker.run(() ->
                orderServiceClient.inquireOrderDetail(userUuid, orderUuid), throwable -> null);
        return MemberOrderResponse.getInstance(member.getUserUuid(), member.getNickname(), member.getProfileImageUrl(), orderDetailResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberOrderResponse findMemberOrderDeliveryTracking(String userUuid, String orderUuid) {
        Member member = memberRepository.findByUserUuid(userUuid).orElseThrow(() -> {
            throw new NoSuchElementException("Invalid Member"); });
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("MS-findMemberOrderDeliveryTracking-circuitbreaker");
        DeliveryTrackingResponse deliveryTrackingResponse = circuitBreaker.run(() ->
                orderServiceClient.inquireDeliveryTracking(userUuid, orderUuid), throwable -> null);
        return MemberOrderResponse.getInstance(member.getUserUuid(), member.getNickname(), member.getProfileImageUrl(), deliveryTrackingResponse);
    }
}
