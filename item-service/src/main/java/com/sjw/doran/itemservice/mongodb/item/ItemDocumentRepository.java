package com.sjw.doran.itemservice.mongodb.item;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemDocumentRepository extends ItemDocumentCustomRepository, MongoRepository<ItemDocument, Long> {
}
