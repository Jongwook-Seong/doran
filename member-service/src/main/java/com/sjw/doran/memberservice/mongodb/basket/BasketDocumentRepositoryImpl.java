package com.sjw.doran.memberservice.mongodb.basket;

import com.sjw.doran.memberservice.kafka.basket.BasketTopicMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BasketDocumentRepositoryImpl implements BasketDocumentCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public void addBasketItem(Long basketId, BasketTopicMessage.BasketItemData item) {
        Query query = new Query(Criteria.where("id").is(basketId));
        Update update = new Update().addToSet("items", item);
        mongoTemplate.updateFirst(query, update, BasketDocument.class);
    }

    @Override
    public void deleteBasketItemByItemUuid(Long basketId, String itemUuid) {
        Query query = new Query(Criteria.where("id").is(basketId));
        Update update = new Update().pull("items", Query.query(Criteria.where("itemUuid").is(itemUuid)));
        mongoTemplate.updateFirst(query, update, BasketDocument.class);
    }

    @Override
    public void deleteAllById(Long basketId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(basketId));
        mongoTemplate.remove(query, BasketDocument.class);
    }
}
