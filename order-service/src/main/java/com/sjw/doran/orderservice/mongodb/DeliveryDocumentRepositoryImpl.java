package com.sjw.doran.orderservice.mongodb;

import com.sjw.doran.orderservice.entity.DeliveryStatus;
import com.sjw.doran.orderservice.kafka.delivery.DeliveryTopicMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryDocumentRepositoryImpl implements DeliveryDocumentCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public void addDeliveryTracking(Long deliveryId, DeliveryTopicMessage.DeliveryTrackingData deliveryTrackingData, DeliveryStatus deliveryStatus) {
        Query query = new Query(Criteria.where("id").is(deliveryId));
        Update update = new Update()
                .addToSet("deliveryTrackings", deliveryTrackingData)
                .set("deliveryStatus", deliveryStatus);
        mongoTemplate.updateFirst(query, update, DeliveryDocument.class);
    }
}
