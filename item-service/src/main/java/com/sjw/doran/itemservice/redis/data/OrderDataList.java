package com.sjw.doran.itemservice.redis.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@RedisHash(value = "item_order_data", timeToLive = 10)
public class OrderDataList {

    @Id
    private String itemUuid;
    private List<OrderData> orderData;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderData {
        private int orderQuantity;
        private LocalDateTime orderDateTime;
    }
}
