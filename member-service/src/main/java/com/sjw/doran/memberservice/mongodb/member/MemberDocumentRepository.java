package com.sjw.doran.memberservice.mongodb.member;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberDocumentRepository extends MemberDocumentCustomRepository, MongoRepository<MemberDocument, Long> {
}
