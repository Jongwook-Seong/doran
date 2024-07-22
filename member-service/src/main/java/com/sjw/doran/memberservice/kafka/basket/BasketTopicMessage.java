package com.sjw.doran.memberservice.kafka.basket;

import com.sjw.doran.memberservice.entity.Category;
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
        private String itemName;
        private String itemImageUrl;
        private Category category;
        private int price;
        private int count;

        public BasketItemData(String itemUuid, int count) {
            this.itemUuid = itemUuid;
            this.count = count;
        }
    }
}
