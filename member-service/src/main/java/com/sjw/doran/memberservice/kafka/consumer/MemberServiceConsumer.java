package com.sjw.doran.memberservice.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sjw.doran.memberservice.kafka.basket.BasketTopicMessage;
import com.sjw.doran.memberservice.kafka.common.BasketOperationType;
import com.sjw.doran.memberservice.kafka.common.OperationType;
import com.sjw.doran.memberservice.kafka.common.Topic;
import com.sjw.doran.memberservice.kafka.member.MemberTopicMessage;
import com.sjw.doran.memberservice.mapper.BasketMapper;
import com.sjw.doran.memberservice.mapper.CustomObjectMapper;
import com.sjw.doran.memberservice.mongodb.BasketDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceConsumer {

    private final CustomObjectMapper objectMapper = new CustomObjectMapper();
    private final BasketMapper basketMapper;
    private final BasketDocumentRepository basketDocumentRepository;

    @KafkaListener(topics = {Topic.MEMBER_TOPIC }, groupId = "member-consumer-group", concurrency = "3")
    public void listenMemberTopic(ConsumerRecord<String, String> record) throws JsonProcessingException {
        MemberTopicMessage message = objectMapper.readValue(record.value(), MemberTopicMessage.class);
        if (message.getOperationType() == OperationType.CREATE) {
            handleMemberCreate(message);
        } else if (message.getOperationType() == OperationType.UPDATE) {
            handleMemberUpdate(message);
        } else if (message.getOperationType() == OperationType.DELETE) {
            handleMemberDelete(message);
        }
    }

    @KafkaListener(topics = { Topic.BASKET_TOPIC }, groupId = "member-consumer-group", concurrency = "3")
    public void listenBasketTopic(ConsumerRecord<String, String> record) throws JsonProcessingException {
        BasketTopicMessage message = objectMapper.readValue(record.value(), BasketTopicMessage.class);
        if (message.getOperationType() == BasketOperationType.CREATE) {
            handleBasketCreate(message);
        } else if (message.getOperationType() == BasketOperationType.ADD_ITEM) {
            handleBasketItemAdd(message);
        } else if (message.getOperationType() == BasketOperationType.REMOVE_ITEM) {
            handleBasketItemRemove(message);
        } else if (message.getOperationType() == BasketOperationType.DELETE) {
            handleBasketDelete(message);
        }
    }

    private void handleMemberCreate(MemberTopicMessage message) {
        try {
            log.info("[{}] Consumed MemberTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(message.getPayload()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleMemberUpdate(MemberTopicMessage message) {
        try {
            log.info("[{}] Consumed MemberTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(message.getPayload()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleMemberDelete(MemberTopicMessage message) {
        try {
            log.info("[{}] Consumed MemberTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(message.getPayload()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleBasketCreate(BasketTopicMessage message) {
        try {
            BasketTopicMessage.Payload payload = message.getPayload();
            log.info("[{}] Consumed BasketTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(payload));
            basketDocumentRepository.save(basketMapper.toBasketDocument(payload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleBasketItemAdd(BasketTopicMessage message) {
        try {
            BasketTopicMessage.Payload payload = message.getPayload();
            log.info("[{}] Consumed BasketTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(payload));
            basketDocumentRepository.addBasketItem(payload.getId(), payload.getBasketItems().get(0));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleBasketItemRemove(BasketTopicMessage message) {
        try {
            BasketTopicMessage.Payload payload = message.getPayload();
            log.info("[{}] Consumed BasketTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(payload));
            basketDocumentRepository.deleteBasketItemByItemUuid(payload.getId(), payload.getBasketItems().get(0).getItemUuid());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleBasketDelete(BasketTopicMessage message) {
        try {
            BasketTopicMessage.Payload payload = message.getPayload();
            log.info("[{}] Consumed BasketTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(payload));
            basketDocumentRepository.delete(basketMapper.toBasketDocument(payload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
