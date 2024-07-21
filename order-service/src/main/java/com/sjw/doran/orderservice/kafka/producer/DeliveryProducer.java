package com.sjw.doran.orderservice.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sjw.doran.orderservice.entity.Delivery;
import com.sjw.doran.orderservice.entity.DeliveryTracking;
import com.sjw.doran.orderservice.kafka.common.OperationType;
import com.sjw.doran.orderservice.kafka.common.Topic;
import com.sjw.doran.orderservice.kafka.delivery.DeliveryTopicMessage;
import com.sjw.doran.orderservice.mapper.CustomObjectMapper;
import com.sjw.doran.orderservice.mapper.DeliveryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeliveryProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();
    private final DeliveryMapper deliveryMapper;

    public void sendMessage(DeliveryTopicMessage message) {
        try {
            kafkaTemplate.send(Topic.DELIVERY_TOPIC, String.valueOf(message.getId()), objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCreateMessage(Delivery delivery, List<DeliveryTracking> deliveryTrackings) {
        DeliveryTopicMessage message = deliveryMapper.toDeliveryTopicMessage(delivery, deliveryTrackings, OperationType.CREATE);
        sendMessage(message);
    }

    public void sendUpdateMessage(Delivery delivery, List<DeliveryTracking> deliveryTrackings) {
        DeliveryTopicMessage message = deliveryMapper.toDeliveryTopicMessage(delivery, deliveryTrackings, OperationType.UPDATE);
        sendMessage(message);
    }

    public void sendDeleteMessage(Long deliveryId) {
        // TODO: deliveryId를 파라미터로 어떻게 전달할지 해결 필요
        DeliveryTopicMessage message = deliveryMapper.toDeliveryTopicMessage(null, null, OperationType.DELETE);
        sendMessage(message);
    }
}
