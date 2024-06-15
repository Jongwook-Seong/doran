package com.sjw.doran.memberservice.service;

import com.sjw.doran.memberservice.dto.MemberDto;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.vo.response.MemberOrderResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberService {

    @Transactional(readOnly = true)
    Member findMember(String userUuid);
    @Transactional(readOnly = true)
    MemberDto findMemberDto(String userUuid);
    @Transactional(readOnly = true)
    List<MemberDto> findMembers();
    @Transactional
    void saveMember(Member member);
    @Transactional
    void deleteMember(String userUuid);

    @Transactional(readOnly = true)
    MemberOrderResponse findMemberOrderList(String userUuid) throws InterruptedException;

    @Transactional(readOnly = true)
    MemberOrderResponse findMemberOrderDetail(String userUuid, String orderUuid) throws InterruptedException;

    @Transactional(readOnly = true)
    MemberOrderResponse findMemberOrderDeliveryTracking(String userUuid, String orderUuid) throws InterruptedException;
}
