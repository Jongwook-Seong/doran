package com.sjw.doran.itemservice.mongodb.incidental;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class IncidentalDocumentRepositoryImpl implements IncidentalDocumentCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public IncidentalDocument findByItemUuidWithinAnHour(String itemUuid) {
        MatchOperation matchItemUuid = Aggregation.match(Criteria.where("_id").is(itemUuid));
        UnwindOperation unwindIncidentalDataList = Aggregation.unwind("incidentalDataList");
        MatchOperation matchWithinAnHour = Aggregation.match(Criteria.where("incidentalDataList.orderDateTime")
                .gte(LocalDateTime.now().minusDays(1L)));
        Aggregation aggregation = Aggregation.newAggregation(matchItemUuid, unwindIncidentalDataList, matchWithinAnHour,
                Aggregation.project("itemUuid", "incidentalDataList")
                        .and("incidentalDataList.orderQuantity").as("orderQuantity")
                        .and("incidentalDataList.orderDateTime").as("orderDateTime"));

        AggregationResults<IncidentalDocument> results =
                mongoTemplate.aggregate(aggregation, "incidentalItemOrderInfo", IncidentalDocument.class);

        List<IncidentalDocument.OrderIncidentalData> filteredDataList = results.getMappedResults().stream()
                .map(IncidentalDocument::getIncidentalDataList)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return new IncidentalDocument(itemUuid, filteredDataList);
    }

    @Override
    public void saveAnIncidentalData(String itemUuid, int orderQuantity) {
        Query query = new Query(Criteria.where("_id").is(itemUuid));
        if (!mongoTemplate.exists(query, IncidentalDocument.class))
            mongoTemplate.save(new IncidentalDocument(itemUuid, null));

        Update update = new Update().addToSet("incidentalDataList",
                new IncidentalDocument.OrderIncidentalData(orderQuantity, LocalDateTime.now()));
        mongoTemplate.updateFirst(query, update, IncidentalDocument.class);
    }

    @Override
    public void deleteOlderThanOneHour() {
        Query query = new Query();
        Update update = new Update().pull("incidentalDataList",
                new Query(Criteria.where("orderDateTime").lt(LocalDateTime.now().minusDays(1L))));
        mongoTemplate.updateMulti(query, update, IncidentalDocument.class);
    }
}
