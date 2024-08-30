package com.sjw.doran.memberservice.mongodb.item;

import com.sjw.doran.memberservice.kafka.item.ItemTopicMessage;

import java.util.List;

public interface ItemDocumentCustomRepository {

    List<ItemDocument> findAllByItemUuidIn(List<String> itemUuidList);

    void updateItemDocument(Long itemDocumentId, ItemTopicMessage.Payload payload);
}
