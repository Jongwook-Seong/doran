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
import com.sjw.doran.memberservice.mapper.order.DeliveryMapper;
import com.sjw.doran.memberservice.mapper.order.OrderMapper;
import com.sjw.doran.memberservice.mongodb.delivery.DeliveryDocument;
import com.sjw.doran.memberservice.mongodb.delivery.DeliveryDocumentRepository;
import com.sjw.doran.memberservice.mongodb.item.ItemDocument;
import com.sjw.doran.memberservice.mongodb.item.ItemDocumentRepository;
import com.sjw.doran.memberservice.mongodb.member.MemberDocument;
import com.sjw.doran.memberservice.mongodb.member.MemberDocumentRepository;
import com.sjw.doran.memberservice.mongodb.order.OrderDocument;
import com.sjw.doran.memberservice.mongodb.order.OrderDocumentRepository;
import com.sjw.doran.memberservice.repository.BasketRepository;
import com.sjw.doran.memberservice.repository.MemberRepository;
import com.sjw.doran.memberservice.service.MemberService;
import com.sjw.doran.memberservice.util.MessageUtil;
import com.sjw.doran.memberservice.vo.response.MemberOrderResponse;
import com.sjw.doran.memberservice.vo.response.order.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BasketRepository basketRepository;
    private final MemberDocumentRepository memberDocumentRepository;
    private final OrderDocumentRepository orderDocumentRepository;
    private final DeliveryDocumentRepository deliveryDocumentRepository;
    private final ItemDocumentRepository itemDocumentRepository;
    private final ResilientOrderServiceClient resilientOrderServiceClient;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final MemberMapper memberMapper;
    private final BasketMapper basketMapper;
    private final OrderMapper orderMapper;
    private final DeliveryMapper deliveryMapper;
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
//        List<Member> members = memberRepository.findAll();
//        List<MemberDto> memberDtos = memberMapper.toMemberDtoList(members);
        List<MemberDocument> memberDocuments = memberDocumentRepository.findAll();
        List<MemberDto> memberDtos = memberMapper.toMemberDtoListFromMemberDocumentList(memberDocuments);
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
            applicationEventPublisher.publishEvent(new BasketEvent(this, basket.getId(),
                    new BasketTopicMessage.Payload(basket.getId(), userUuid, null), BasketOperationType.DELETE));
        } catch (Exception e) {
            throw new RuntimeException(messageUtil.getMemberDeleteErrorMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MemberOrderResponse findMemberOrderList(String userUuid) throws InterruptedException {
        Member member = memberRepository.findByUserUuid(userUuid).orElseThrow(() -> {
            throw new NoSuchElementException("Invalid Member"); });
        // Get Order-Service data by using Kafka Asynchronous Communication, instead of FeignClient
        List<OrderDocument> orderDocuments = orderDocumentRepository.findAllByUserUuid(userUuid);
        List<Long> deliveryIds = orderDocuments.stream().map(OrderDocument::getDeliveryId).collect(Collectors.toList());
        // extract deliveryStatus list for mapping
        List<DeliveryDocument> deliveryDocuments = deliveryDocumentRepository.findAllByIds(deliveryIds);
        List<DeliveryStatus> deliveryStatuses = deliveryDocuments.stream()
                .map(DeliveryDocument::getDeliveryStatus).collect(Collectors.toList());
        // extract itemDocuments list for mapping
        List<List<OrderDocument.OrderItem>> orderItems = orderDocuments.stream().map(OrderDocument::getOrderItems).collect(Collectors.toList());
        List<List<String>> ordersItemUuids = new ArrayList<>();
        orderItems.stream().forEach(list -> ordersItemUuids.add(list.stream()
                .map(OrderDocument.OrderItem::getItemUuid).collect(Collectors.toList())));
        List<String> itemUuidList = new ArrayList<>();
        ordersItemUuids.forEach(list -> list.forEach(itemUuid -> itemUuidList.add(itemUuid)));
        List<ItemDocument> itemDocuments = itemDocumentRepository.findAllByItemUuidIn(itemUuidList);
        List<List<ItemDocument>> itemDocumentsList = extractItemDocumentsList(itemDocuments, ordersItemUuids);
        // create orderListResponse by mapping orderSimpleList
        List<OrderSimple> orderSimpleList = orderMapper.toOrderSimpleList(orderDocuments, itemDocumentsList, deliveryStatuses);
        OrderListResponse orderListResponse = OrderListResponse.getInstance(orderSimpleList);
//        OrderListResponse orderListResponse = resilientOrderServiceClient.inquireOrderList(userUuid);
        return MemberOrderResponse.getInstance(member.getUserUuid(), member.getNickname(), member.getProfileImageUrl(), orderListResponse);
    }

    private List<List<ItemDocument>> extractItemDocumentsList(List<ItemDocument> itemDocuments, List<List<String>> ordersItemUuids) {
        Map<String, ItemDocument> itemDocumentMap = itemDocuments.stream().collect(Collectors.toMap(ItemDocument::getItemUuid, Function.identity()));
        List<List<ItemDocument>> itemDocumentsList = new ArrayList<>();
        ordersItemUuids.forEach(orderItemUuids -> itemDocumentsList.add(orderItemUuids.stream()
                .map(itemDocumentMap::get).collect(Collectors.toList())));
        return itemDocumentsList;
    }

    @Override
    @Transactional(readOnly = true)
    public MemberOrderResponse findMemberOrderDetail(String userUuid, String orderUuid) throws InterruptedException {
        Member member = memberRepository.findByUserUuid(userUuid).orElseThrow(() -> {
            throw new NoSuchElementException("Invalid Member"); });
        // Get Order-Service data by using Kafka Asynchronous Communication, instead of FeignClient
        OrderDocument orderDocument = orderDocumentRepository.findByUserUuidAndOrderUuid(userUuid, orderUuid).orElseThrow();
        DeliveryDocument deliveryDocument = deliveryDocumentRepository.findById(orderDocument.getDeliveryId()).orElseThrow();
        OrderDetailResponse orderDetailResponse = orderMapper.toOrderDetailResponse(orderDocument, deliveryDocument);
//        OrderDetailResponse orderDetailResponse = resilientOrderServiceClient.inquireOrderDetail(userUuid, orderUuid);
        return MemberOrderResponse.getInstance(member.getUserUuid(), member.getNickname(), member.getProfileImageUrl(), orderDetailResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberOrderResponse findMemberOrderDeliveryTracking(String userUuid, String orderUuid) throws InterruptedException {
        Member member = memberRepository.findByUserUuid(userUuid).orElseThrow(() -> {
            throw new NoSuchElementException("Invalid Member"); });
        // Get Order-Service data by using Kafka Asynchronous Communication, instead of FeignClient
        OrderDocument orderDocument = orderDocumentRepository.findByUserUuidAndOrderUuid(userUuid, orderUuid).orElseThrow();
        DeliveryDocument deliveryDocument = deliveryDocumentRepository.findById(orderDocument.getDeliveryId()).orElseThrow();
        DeliveryTrackingResponse deliveryTrackingResponse = deliveryMapper.toDeliveryTrackingResponse(deliveryDocument);
//        DeliveryTrackingResponse deliveryTrackingResponse = resilientOrderServiceClient.inquireDeliveryTracking(userUuid, orderUuid);
        return MemberOrderResponse.getInstance(member.getUserUuid(), member.getNickname(), member.getProfileImageUrl(), deliveryTrackingResponse);
    }
}
