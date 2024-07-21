package com.sjw.doran.memberservice.kafka.basket;

import com.sjw.doran.memberservice.kafka.common.BasketOperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketTopicMessage {

    private Long id;
    private Payload payload;
    private BasketOperationType operationType;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload {

        private Long id;
        private List<BasketItemData> basketItems;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasketItemData {

        private String itemUuid;
        private int count;
    }
}
