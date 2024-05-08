package com.sjw.doran.orderservice.vo.response;

import lombok.Data;

@Data
public class ItemSimpleWithoutPriceResponse {

    private String itemUuid;
    private String itemName;
    private String itemImageUrl;
}
