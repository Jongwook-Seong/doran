package com.sjw.doran.orderservice.mongodb;

import com.sjw.doran.orderservice.entity.DeliveryStatus;
import com.sjw.doran.orderservice.kafka.delivery.DeliveryTopicMessage;

public interface DeliveryDocumentCustomRepository {

    void addDeliveryTracking(Long deliveryId, DeliveryTopicMessage.DeliveryTrackingData deliveryTrackingData, DeliveryStatus deliveryStatus);
}
