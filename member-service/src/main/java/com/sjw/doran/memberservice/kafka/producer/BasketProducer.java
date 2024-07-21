package com.sjw.doran.memberservice.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sjw.doran.memberservice.kafka.basket.BasketTopicMessage;
import com.sjw.doran.memberservice.kafka.common.Topic;
import com.sjw.doran.memberservice.mapper.CustomObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BasketProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    public void sendMessage(BasketTopicMessage message) {
        try {
            kafkaTemplate.send(Topic.BASKET_TOPIC, String.valueOf(message.getId()), objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
