package com.sjw.doran.orderservice.vo;

import com.sjw.doran.orderservice.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSimpleInfo {

    private String itemUuid;
    private int count;
    private int price;

    public static ItemSimpleInfo getInstance(OrderItem orderItem) {
        return ItemSimpleInfo.builder()
                .itemUuid(orderItem.getItemUuid())
                .count(orderItem.getCount())
                .price(orderItem.getOrderPrice())
                .build();
    }
}
