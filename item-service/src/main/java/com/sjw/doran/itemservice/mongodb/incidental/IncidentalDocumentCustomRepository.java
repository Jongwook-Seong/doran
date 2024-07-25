package com.sjw.doran.itemservice.mongodb.incidental;

public interface IncidentalDocumentCustomRepository {

    IncidentalDocument findByItemUuidWithinAnHour(String itemUuid);
    void saveAnIncidentalData(String itemUuid, int orderQuantity);
    void deleteOlderThanOneHour();
}
