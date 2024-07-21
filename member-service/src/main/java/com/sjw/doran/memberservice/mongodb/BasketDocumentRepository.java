package com.sjw.doran.memberservice.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BasketDocumentRepository extends BasketDocumentCustomRepository, MongoRepository<BasketDocument, Long> {
}
