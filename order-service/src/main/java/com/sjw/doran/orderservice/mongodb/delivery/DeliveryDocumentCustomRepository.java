package com.sjw.doran.orderservice.mongodb.delivery;

import com.sjw.doran.orderservice.entity.DeliveryStatus;
import com.sjw.doran.orderservice.kafka.delivery.DeliveryTopicMessage;

import java.util.List;

public interface DeliveryDocumentCustomRepository {

    void addDeliveryTracking(Long deliveryId, DeliveryTopicMessage.DeliveryTrackingData deliveryTrackingData, DeliveryStatus deliveryStatus);

    List<DeliveryDocument> findAllByIds(List<Long> deliveryIds);
}
