package com.sjw.doran.memberservice.mongodb.order;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<OrderDocument> findByUserUuidAndOrderUuid(String userUuid, String orderUuid) {
        Query query = new Query(Criteria.where("userUuid").is(userUuid).and("orderUuid").is(orderUuid));
        return Optional.ofNullable(mongoTemplate.findOne(query, OrderDocument.class));
    }
}
