package com.sjw.doran.memberservice.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sjw.doran.memberservice.kafka.common.OperationType;
import com.sjw.doran.memberservice.kafka.common.Topic;
import com.sjw.doran.memberservice.kafka.delivery.DeliveryTopicMessage;
import com.sjw.doran.memberservice.kafka.order.OrderTopicMessage;
import com.sjw.doran.memberservice.mapper.CustomObjectMapper;
import com.sjw.doran.memberservice.mapper.order.DeliveryMapper;
import com.sjw.doran.memberservice.mapper.order.OrderMapper;
import com.sjw.doran.memberservice.mongodb.delivery.DeliveryDocumentRepository;
import com.sjw.doran.memberservice.mongodb.order.OrderDocumentRepository;
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
    private final OrderMapper orderMapper;
    private final DeliveryMapper deliveryMapper;
    private final OrderDocumentRepository orderDocumentRepository;
    private final DeliveryDocumentRepository deliveryDocumentRepository;

    @KafkaListener(topics = {Topic.ORDER_TOPIC}, groupId = "member-svc-order-consumer-group", concurrency = "2")
    public void listenOrderTopic(ConsumerRecord<String, String> record) throws JsonProcessingException {
        OrderTopicMessage message = objectMapper.readValue(record.value(), OrderTopicMessage.class);
        if (message.getOperationType() == OperationType.CREATE) {
            handleOrderCreate(message);
        } else if (message.getOperationType() == OperationType.DELETE) {
            handleOrderDelete(message);
        }
    }

    @KafkaListener(topics = { Topic.DELIVERY_TOPIC }, groupId = "member-svc-delivery-consumer-group", concurrency = "2")
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
            OrderTopicMessage.Payload payload = message.getPayload();
            log.info("[{}] Consumed OrderTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(payload));
            orderDocumentRepository.save(orderMapper.toOrderDocument(payload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleOrderDelete(OrderTopicMessage message) {
        try {
            log.info("[{}] Consumed OrderTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(message.getPayload()));
            orderDocumentRepository.deleteById(message.getId());
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
            deliveryDocumentRepository.deleteById(message.getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
