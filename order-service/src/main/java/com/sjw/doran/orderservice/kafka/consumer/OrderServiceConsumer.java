package com.sjw.doran.orderservice.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sjw.doran.orderservice.kafka.common.OperationType;
import com.sjw.doran.orderservice.kafka.common.Topic;
import com.sjw.doran.orderservice.kafka.delivery.DeliveryTopicMessage;
import com.sjw.doran.orderservice.kafka.order.OrderTopicMessage;
import com.sjw.doran.orderservice.mapper.CustomObjectMapper;
import com.sjw.doran.orderservice.mapper.DeliveryMapper;
import com.sjw.doran.orderservice.mongodb.DeliveryDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceConsumer {

    private final CustomObjectMapper objectMapper = new CustomObjectMapper();
    private final DeliveryMapper deliveryMapper;
    private final DeliveryDocumentRepository deliveryDocumentRepository;

    @KafkaListener(topics = { Topic.ORDER_TOPIC }, groupId = "order-consumer-group", concurrency = "3")
    public void listenOrderTopic(ConsumerRecord<String, String> record) throws JsonProcessingException {
        OrderTopicMessage message = objectMapper.readValue(record.value(), OrderTopicMessage.class);
        if (message.getOperationType() == OperationType.CREATE) {
            handleOrderCreate(message);
        } else if (message.getOperationType() == OperationType.UPDATE) {
            handleOrderUpdate(message);
        } else if (message.getOperationType() == OperationType.DELETE) {
            handleOrderDelete(message);
        }
    }

    @KafkaListener(topics = { Topic.DELIVERY_TOPIC }, groupId = "order-consumer-group", concurrency = "3")
    public void listenDeliveryTopic(ConsumerRecord<String, String> record) throws JsonProcessingException {
        DeliveryTopicMessage message = objectMapper.readValue(record.value(), DeliveryTopicMessage.class);
        if (message.getOperationType() == OperationType.CREATE) {
            handleDeliveryCreate(message);
        } else if (message.getOperationType() == OperationType.UPDATE) {
            handleDeliveryUpdate(message);
        } else if (message.getOperationType() == OperationType.DELETE) {
            handleDeliveryDelete(message);
        }
    }

    private void handleOrderCreate(OrderTopicMessage message) {
        try {
            log.info("[{}] Consumed OrderTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(message.getPayload()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleOrderUpdate(OrderTopicMessage message) {
        try {
            log.info("[{}] Consumed OrderTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(message.getPayload()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleOrderDelete(OrderTopicMessage message) {
        try {
            log.info("[{}] Consumed OrderTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(message.getPayload()));
            deliveryDocumentRepository.deleteById(message.getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeliveryCreate(DeliveryTopicMessage message) {
        try {
            DeliveryTopicMessage.Payload payload = message.getPayload();
            log.info("[{}] Consumed DeliveryTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(payload));
            deliveryDocumentRepository.save(deliveryMapper.toDeliveryDocument(payload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeliveryUpdate(DeliveryTopicMessage message) {
        try {
            DeliveryTopicMessage.Payload payload = message.getPayload();
            log.info("[{}] Consumed DeliveryTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(message.getPayload()));
            deliveryDocumentRepository.addDeliveryTracking(message.getId(), payload.getDeliveryTrackings().get(0), payload.getDeliveryStatus());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeliveryDelete(DeliveryTopicMessage message) {
        try {
            log.info("[{}] Consumed DeliveryTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(message.getPayload()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
