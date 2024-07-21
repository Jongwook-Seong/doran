package com.sjw.doran.memberservice.mongodb;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "basketItemList")
@Data
@AllArgsConstructor
public class BasketDocument {

    @Id
    private Long id;
    private List<Item> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {

        private String itemUuid;
        private int count;
    }
}
