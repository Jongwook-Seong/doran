package com.sjw.doran.memberservice.mongodb.delivery;

import com.sjw.doran.memberservice.kafka.delivery.DeliveryTopicMessage;
import com.sjw.doran.memberservice.vo.response.order.DeliveryStatus;

import java.util.List;
import java.util.Optional;

public interface DeliveryDocumentCustomRepository {

    void addDeliveryTracking(Long deliveryId, DeliveryTopicMessage.DeliveryTrackingData deliveryTrackingData, DeliveryStatus deliveryStatus);

    List<DeliveryDocument> findAllByIds(List<Long> deliveryIds);
}
