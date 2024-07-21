package com.sjw.doran.itemservice.mongodb;

import com.sjw.doran.itemservice.kafka.item.ItemTopicMessage;

import java.util.List;

public interface ItemDocumentCustomRepository {

    void updateBookInfo(Long itemId, ItemTopicMessage.Payload payload);
    void updateArtworkInfo(Long itemId, ItemTopicMessage.Payload payload);
    void updateItemDocument(Long itemId, ItemTopicMessage.Payload payload);
    ItemDocument findByItemUuid(String itemUuid);
    List<ItemDocument> findByItemUuidList(List<String> itemUuidList);
    List<ItemDocument> findBooksByKeyword(String keyword);
    List<ItemDocument> findByKeyword(String keyword);
}
