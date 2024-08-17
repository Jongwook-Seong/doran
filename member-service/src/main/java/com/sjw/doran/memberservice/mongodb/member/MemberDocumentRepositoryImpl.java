package com.sjw.doran.memberservice.mongodb.member;

import com.sjw.doran.memberservice.kafka.member.MemberTopicMessage;
import com.sjw.doran.memberservice.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class MemberDocumentRepositoryImpl implements MemberDocumentCustomRepository {

    private final MongoTemplate mongoTemplate;
    private final MemberMapper memberMapper;

    @Override
    public void updateMemberDocument(Long memberId, MemberTopicMessage.Payload payload) {
        Query query = new Query(Criteria.where("id").is(memberId));
        Update update =new Update()
                .set("nickname", payload.getNickname())
                .set("profileImageUrl", payload.getProfileImageUrl())
                .set("address", memberMapper.toAddress(payload.getAddress()))
                .set("modifiedAt", LocalDateTime.now());
        mongoTemplate.updateFirst(query, update, MemberDocument.class);
    }
}
