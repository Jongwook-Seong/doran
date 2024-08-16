package com.sjw.doran.orderservice.mongodb.order;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderDocumentRepositoryImpl implements OrderDocumentCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<OrderDocument> findAllByUserUuid(String userUuid) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeMonthsAgo = now.minusMonths(3L);

        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid)
                .and("orderDate").gte(threeMonthsAgo).lte(now));
        query.with(Sort.by(Sort.Direction.DESC, "orderDate"));
        return mongoTemplate.find(query, OrderDocument.class);
    }
}
