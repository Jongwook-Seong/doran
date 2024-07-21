package com.sjw.doran.orderservice.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryDocumentRepository extends DeliveryDocumentCustomRepository, MongoRepository<DeliveryDocument, Long> {
}
