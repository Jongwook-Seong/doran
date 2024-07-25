package com.sjw.doran.itemservice.mongodb.incidental;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "incidentalItemOrderInfo")
@Data
@AllArgsConstructor
public class IncidentalDocument {

    @Id
    private String itemUuid;
    private List<OrderIncidentalData> incidentalDataList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderIncidentalData {
        private int orderQuantity;
        private LocalDateTime orderDateTime;
    }
}
