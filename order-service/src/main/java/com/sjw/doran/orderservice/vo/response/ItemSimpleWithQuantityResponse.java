package com.sjw.doran.orderservice.vo.response;

import lombok.Data;

@Data
public class ItemSimpleWithQuantityResponse {

    private String itemUuid;
    private String itemName;
    private int price;
    private int quantity;
    private String itemImageUrl;
}
