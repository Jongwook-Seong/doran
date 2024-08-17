package com.sjw.doran.memberservice.mongodb.member;

import com.sjw.doran.memberservice.kafka.member.MemberTopicMessage;

public interface MemberDocumentCustomRepository {

    void updateMemberDocument(Long memberId, MemberTopicMessage.Payload payload);
}
