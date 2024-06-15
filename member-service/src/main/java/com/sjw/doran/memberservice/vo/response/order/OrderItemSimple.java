package com.sjw.doran.memberservice.vo.response.order;

import jakarta.annotation.Nullable;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemSimple {

    @Nullable//임시
    private String itemName;
    @Nullable//임시
    private String itemImageUrl;
    private String itemUuid;
    private int count;
    private int price;

    public static OrderItemSimple getInstance(String itemUuid, int count, int price) {
        return OrderItemSimple.builder()
                .itemUuid(itemUuid)
                .count(count)
                .price(price)
                .build();
    }
}
