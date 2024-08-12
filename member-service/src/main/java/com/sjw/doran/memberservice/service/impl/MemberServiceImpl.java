package com.sjw.doran.memberservice.service.impl;

import com.sjw.doran.memberservice.client.ResilientOrderServiceClient;
import com.sjw.doran.memberservice.dto.MemberDto;
import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.kafka.basket.BasketEvent;
import com.sjw.doran.memberservice.kafka.basket.BasketTopicMessage;
import com.sjw.doran.memberservice.kafka.common.BasketOperationType;
import com.sjw.doran.memberservice.kafka.common.OperationType;
import com.sjw.doran.memberservice.kafka.member.MemberEvent;
import com.sjw.doran.memberservice.kafka.member.MemberTopicMessage;
import com.sjw.doran.memberservice.mapper.BasketMapper;
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
import org.springframework.context.ApplicationEventPublisher;
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
    private final ResilientOrderServiceClient resilientOrderServiceClient;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final MemberMapper memberMapper;
    private final BasketMapper basketMapper;
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
            Member savedMember = memberRepository.save(member);
            Basket basket = new Basket(member);
            try {
                Basket savedBasket = basketRepository.save(basket);
                /* Publish kafka message */
                MemberTopicMessage memberTopicMessage = memberMapper.toMemberTopicMessage(savedMember, savedMember.getAddress(), null);
                applicationEventPublisher.publishEvent(
                        new MemberEvent(this, memberTopicMessage.getId(), memberTopicMessage.getPayload(), OperationType.CREATE));
                BasketTopicMessage basketTopicMessage = basketMapper.toBasketTopicMessage(savedBasket, null, member.getUserUuid(), null);
                applicationEventPublisher.publishEvent(
                        new BasketEvent(this, basketTopicMessage.getId(), basketTopicMessage.getPayload(), BasketOperationType.CREATE));
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
            Member member = memberRepository.findByUserUuid(userUuid).get();
            Basket basket = member.getBasket();
            memberRepository.delete(member);
            /* Publish kafka message */
            applicationEventPublisher.publishEvent(new MemberEvent(this, member.getId(), null, OperationType.DELETE));
            applicationEventPublisher.publishEvent(new BasketEvent(this, basket.getId(), null, BasketOperationType.DELETE));
        } catch (Exception e) {
            throw new RuntimeException(messageUtil.getMemberDeleteErrorMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MemberOrderResponse findMemberOrderList(String userUuid) throws InterruptedException {
        Member member = memberRepository.findByUserUuid(userUuid).orElseThrow(() -> {
            throw new NoSuchElementException("Invalid Member"); });
        OrderListResponse orderListResponse = resilientOrderServiceClient.inquireOrderList(userUuid);
        return MemberOrderResponse.getInstance(member.getUserUuid(), member.getNickname(), member.getProfileImageUrl(), orderListResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberOrderResponse findMemberOrderDetail(String userUuid, String orderUuid) throws InterruptedException {
        Member member = memberRepository.findByUserUuid(userUuid).orElseThrow(() -> {
            throw new NoSuchElementException("Invalid Member"); });
        OrderDetailResponse orderDetailResponse = resilientOrderServiceClient.inquireOrderDetail(userUuid, orderUuid);
        return MemberOrderResponse.getInstance(member.getUserUuid(), member.getNickname(), member.getProfileImageUrl(), orderDetailResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberOrderResponse findMemberOrderDeliveryTracking(String userUuid, String orderUuid) throws InterruptedException {
        Member member = memberRepository.findByUserUuid(userUuid).orElseThrow(() -> {
            throw new NoSuchElementException("Invalid Member"); });
        DeliveryTrackingResponse deliveryTrackingResponse = resilientOrderServiceClient.inquireDeliveryTracking(userUuid, orderUuid);
        return MemberOrderResponse.getInstance(member.getUserUuid(), member.getNickname(), member.getProfileImageUrl(), deliveryTrackingResponse);
    }
}
