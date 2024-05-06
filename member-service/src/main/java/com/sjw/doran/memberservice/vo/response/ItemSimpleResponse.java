package com.sjw.doran.memberservice.vo.response;

import lombok.Data;

@Data
public class ItemSimpleResponse {

    private String itemUuid;
    private String itemName;
    private String itemImageUrl;
    private int price;
}
