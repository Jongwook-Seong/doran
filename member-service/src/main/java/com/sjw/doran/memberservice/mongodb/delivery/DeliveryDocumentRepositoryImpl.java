package com.sjw.doran.memberservice.mongodb.delivery;

import com.sjw.doran.memberservice.kafka.delivery.DeliveryTopicMessage;
import com.sjw.doran.memberservice.vo.response.order.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public List<DeliveryDocument> findAllByIds(List<Long> deliveryIds) {
        Query query = new Query(Criteria.where("id").in(deliveryIds));
        List<DeliveryDocument> deliveryDocumentList = mongoTemplate.find(query, DeliveryDocument.class);
        Map<Long, DeliveryDocument> deliveryDocMap = deliveryDocumentList.stream()
                .collect(Collectors.toMap(DeliveryDocument::getId, Function.identity()));
        return deliveryIds.stream()
                .map(deliveryDocMap::get)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DeliveryDocument> findByUserUuidAndOrderUuid(String userUuid, String orderUuid) {
        Query query = new Query(Criteria.where("userUuid").is(userUuid).and("orderUuid").is(orderUuid));
        DeliveryDocument deliveryDocument = mongoTemplate.findOne(query, DeliveryDocument.class);
        return Optional.ofNullable(deliveryDocument);
    }
}
