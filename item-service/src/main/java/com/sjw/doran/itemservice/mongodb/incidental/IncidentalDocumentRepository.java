package com.sjw.doran.itemservice.mongodb.incidental;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IncidentalDocumentRepository extends IncidentalDocumentCustomRepository, MongoRepository<IncidentalDocument, String> {
}
