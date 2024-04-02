package com.sjw.doran.orderservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSimpleInfo {

    private String itemUuid;
    private int count;
    private int price;
}
