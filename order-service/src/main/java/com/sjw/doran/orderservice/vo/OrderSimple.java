package com.sjw.doran.orderservice.vo;

import com.sjw.doran.orderservice.entity.DeliveryStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderSimple {

    private List<OrderItemSimple> orderItemSimpleList;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime orderDate;

    public static OrderSimple getInstance(List<OrderItemSimple> orderItemSimpleList, DeliveryStatus deliveryStatus, LocalDateTime orderDate) {
        return OrderSimple.builder()
                .orderItemSimpleList(orderItemSimpleList)
                .deliveryStatus(deliveryStatus)
                .orderDate(orderDate)
                .build();
    }
}
