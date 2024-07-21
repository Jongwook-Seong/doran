package com.sjw.doran.memberservice.mongodb;

import com.sjw.doran.memberservice.kafka.basket.BasketTopicMessage;

public interface BasketDocumentCustomRepository {

//    BasketDocument findById(Long id);
    void addBasketItem(Long basketId, BasketTopicMessage.BasketItemData item);
    void deleteBasketItemByItemUuid(Long basketId, String itemUuid);
    void deleteAllById(Long basketId);
}
