package com.sjw.doran.memberservice.mongodb.delivery;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryDocumentRepository extends DeliveryDocumentCustomRepository, MongoRepository<DeliveryDocument, Long> {
}
