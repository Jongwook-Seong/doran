package com.sjw.doran.memberservice.mongodb.item;

import com.sjw.doran.memberservice.kafka.item.ItemTopicMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemDocumentRepositoryImpl implements ItemDocumentCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<ItemDocument> findAllByItemUuidIn(List<String> itemUuidList) {
        Query query = new Query(Criteria.where("itemUuid").in(itemUuidList));
        List<ItemDocument> itemDocuments = mongoTemplate.find(query, ItemDocument.class);
        Map<String, ItemDocument> itemDocumentMap = itemDocuments.stream()
                .collect(Collectors.toMap(ItemDocument::getItemUuid, Function.identity()));
        return itemUuidList.stream().map(itemDocumentMap::get)
                .collect(Collectors.toList());
    }

    @Override
    public void updateItemDocument(Long itemDocumentId, ItemTopicMessage.Payload payload) {
        Query query = new Query(Criteria.where("id").is(itemDocumentId));
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
}
