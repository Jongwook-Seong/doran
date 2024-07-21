package com.sjw.doran.orderservice.kafka.order;

import com.sjw.doran.orderservice.entity.OrderStatus;
import com.sjw.doran.orderservice.kafka.common.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTopicMessage {

    private Long id;
    private Payload payload;
    private OperationType operationType;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload {

        private Long id;
        private String orderUuid;
        private String userUuid;
        private List<ItemData> items;
        private Long deliveryId;
        private OrderStatus orderStatus;
        private LocalDateTime orderDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemData {

        private String itemUuid;
        private int price;
        private int count;
    }
}
