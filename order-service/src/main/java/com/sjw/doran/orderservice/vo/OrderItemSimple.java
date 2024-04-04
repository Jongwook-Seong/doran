package com.sjw.doran.orderservice.vo;

import com.sjw.doran.orderservice.entity.DeliveryStatus;
import jakarta.annotation.Nullable;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItemSimple {

    @Nullable//임시
    private String itemName;
    @Nullable//임시
    private String itemImageUrl;
    private int count;
    private int price;

    public static OrderItemSimple getInstance(int count, int price) {
        return OrderItemSimple.builder()
                .count(count)
                .price(price)
                .build();
    }
}
