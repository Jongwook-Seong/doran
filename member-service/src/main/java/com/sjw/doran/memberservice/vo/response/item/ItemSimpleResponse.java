package com.sjw.doran.memberservice.vo.response.item;

import com.sjw.doran.memberservice.entity.Category;
import lombok.Data;

@Data
public class ItemSimpleResponse {

    private String itemUuid;
    private String itemName;
    private String itemImageUrl;
    private Category category;
    private int price;
}
