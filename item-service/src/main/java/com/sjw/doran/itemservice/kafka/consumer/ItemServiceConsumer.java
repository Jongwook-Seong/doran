package com.sjw.doran.itemservice.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sjw.doran.itemservice.entity.Category;
import com.sjw.doran.itemservice.kafka.common.OperationType;
import com.sjw.doran.itemservice.kafka.common.Topic;
import com.sjw.doran.itemservice.kafka.item.ItemTopicMessage;
import com.sjw.doran.itemservice.mapper.ArtworkMapper;
import com.sjw.doran.itemservice.mapper.BookMapper;
import com.sjw.doran.itemservice.mapper.CustomObjectMapper;
import com.sjw.doran.itemservice.mongodb.incidental.IncidentalDocumentRepository;
import com.sjw.doran.itemservice.mongodb.item.ItemDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceConsumer {

    private final CustomObjectMapper objectMapper = new CustomObjectMapper();
    private final BookMapper bookMapper;
    private final ArtworkMapper artworkMapper;
    private final ItemDocumentRepository itemDocumentRepository;
    private final IncidentalDocumentRepository incidentalDocumentRepository;

    @KafkaListener(topics = { Topic.ITEM_TOPIC }, groupId = "item-consumer-group", concurrency = "2")
    public void listenItemTopic(ConsumerRecord<String, String> record) throws JsonProcessingException {
        ItemTopicMessage message = objectMapper.readValue(record.value(), ItemTopicMessage.class);
        if (message.getOperationType() == OperationType.CREATE) {
            handleItemCreate(message);
        } else if (message.getOperationType() == OperationType.UPDATE) {
            handleItemUpdate(message);
        } else if (message.getOperationType() == OperationType.DELETE) {
            handleItemDelete(message);
        }
    }

    private void handleItemCreate(ItemTopicMessage message) {
        try {
            ItemTopicMessage.Payload payload = message.getPayload();
            log.info("[{}] Consumed ItemTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(payload));
            if (payload.getCategory() == Category.BOOK) {
                itemDocumentRepository.save(bookMapper.toItemDocument(payload));
            } else if (payload.getCategory() == Category.ARTWORK) {
                itemDocumentRepository.save(artworkMapper.toItemDocument(payload));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleItemUpdate(ItemTopicMessage message) {
        try {
            ItemTopicMessage.Payload payload = message.getPayload();
            log.info("[{}] Consumed ItemTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(payload));
            itemDocumentRepository.updateItemDocument(message.getId(), payload);
            incidentalDocumentRepository.saveAnIncidentalData(payload.getItemUuid(), payload.getLatestOrderQuantity());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleItemDelete(ItemTopicMessage message) {
        try {
            log.info("[{}] Consumed ItemTopicMessage: {}",
                    message.getOperationType(), objectMapper.writeValueAsString(message.getPayload()));
            itemDocumentRepository.deleteById(message.getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
