package com.sjw.doran.orderservice.dto;

import com.sjw.doran.orderservice.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private String orderUuid;
    private String userUuid;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;

    public static OrderDto getInstanceForCreate(String userUuid) {
        return OrderDto.builder()
                .orderUuid(UUID.randomUUID().toString())
                .userUuid(userUuid)
                .orderStatus(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now())
                .build();
    }
}
