package com.sjw.doran.orderservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSimpleWithQuantityResponse {

    private String itemUuid;
    private String itemName;
    private int price;
    private int quantity;
    private String itemImageUrl;
}
