package com.sjw.doran.itemservice.redis.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@RedisHash(value = "ordered_item_sales", timeToLive = 7200)
public class OrderedItemSales {

    @Id
    private String itemUuid;
    private Long sales;
}
