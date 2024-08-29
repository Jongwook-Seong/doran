package com.sjw.doran.memberservice.mongodb.order;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderDocumentRepository extends OrderDocumentCustomRepository, MongoRepository<OrderDocument, Long> {
}
