package com.sjw.doran.itemservice.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sjw.doran.itemservice.kafka.common.Topic;
import com.sjw.doran.itemservice.kafka.item.ItemTopicMessage;
import com.sjw.doran.itemservice.mapper.CustomObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    public void sendMessage(ItemTopicMessage message) {
        try {
            kafkaTemplate.send(Topic.ITEM_TOPIC, String.valueOf(message.getId()), objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
