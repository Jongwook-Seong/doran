package com.sjw.doran.orderservice.dto;

import com.sjw.doran.orderservice.vo.ItemSimpleInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private String itemUuid;
    private int orderPrice;
    private int count;

    public static OrderItemDto getInstanceForCreate(ItemSimpleInfo itemSimpleInfo) {
        return OrderItemDto.builder()
                .itemUuid(itemSimpleInfo.getItemUuid())
                .orderPrice(itemSimpleInfo.getPrice() * itemSimpleInfo.getCount())
                .count(itemSimpleInfo.getPrice())
                .build();
    }
}
