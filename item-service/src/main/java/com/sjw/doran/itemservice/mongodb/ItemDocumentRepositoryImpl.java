package com.sjw.doran.itemservice.mongodb;

import com.sjw.doran.itemservice.entity.Category;
import com.sjw.doran.itemservice.kafka.item.ItemTopicMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemDocumentRepositoryImpl implements ItemDocumentCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public void updateBookInfo(Long itemId, ItemTopicMessage.Payload payload) {
        Query query = new Query(Criteria.where("id").is(itemId));
        Update update = new Update()
                .set("price", payload.getPrice())
                .set("stockQuantity", payload.getStockQuantity())
                .set("itemImageUrl", payload.getItemImageUrl())
                .set("bookInfo", payload.getBookData());
        mongoTemplate.updateFirst(query, update, ItemDocument.class);
    }

    @Override
    public void updateArtworkInfo(Long itemId, ItemTopicMessage.Payload payload) {
        Query query = new Query(Criteria.where("id").is(itemId));
        Update update = new Update()
                .set("price", payload.getPrice())
                .set("stockQuantity", payload.getStockQuantity())
                .set("itemImageUrl", payload.getItemImageUrl())
                .set("artworkInfo", payload.getArtworkData());
        mongoTemplate.updateFirst(query, update, ItemDocument.class);
    }

    @Override
    public void updateItemDocument(Long itemId, ItemTopicMessage.Payload payload) {
        Query query = new Query(Criteria.where("id").is(itemId));
        Update update = new Update()
                .set("itemName", payload.getItemName())
                .set("price", payload.getPrice())
                .set("stockQuantity", payload.getStockQuantity())
                .set("itemImageUrl", payload.getItemImageUrl())
                .set("category", payload.getCategory())
                .set("bookInfo", payload.getBookData())
                .set("artworkInfo", payload.getArtworkData())
                .set("modifiedAt", payload.getModifiedAt());
        mongoTemplate.updateFirst(query, update, ItemDocument.class);
    }

    @Override
    public ItemDocument findByItemUuid(String itemUuid) {
        Query query = new Query(Criteria.where("itemUuid").is(itemUuid));
        return mongoTemplate.findOne(query, ItemDocument.class);
    }

    @Override
    public List<ItemDocument> findByItemUuidList(List<String> itemUuidList) {
        Query query = new Query(Criteria.where("itemUuid").in(itemUuidList));
        return mongoTemplate.find(query, ItemDocument.class);
    }

    @Override
    public List<ItemDocument> findBooksByKeyword(String keyword) {
        Query query = new Query(Criteria.where("itemName")
                .orOperator(Criteria.where("bookInfo.author"))
                .andOperator(Criteria.where("category").is(Category.BOOK))
                .regex(keyword, "i"));
        return mongoTemplate.find(query, ItemDocument.class);
    }

    @Override
    public List<ItemDocument> findByKeyword(String keyword) {
        Query query = new Query(Criteria.where("itemName")
                .orOperator(Criteria.where("bookInfo.author"))
                .orOperator(Criteria.where("artworkInfo.artist"))
                .orOperator(Criteria.where("artworkInfo.explanation"))
                .regex(keyword, "i"));
        return mongoTemplate.find(query, ItemDocument.class);
    }
}
