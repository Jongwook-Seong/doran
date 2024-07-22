package com.sjw.doran.memberservice.redis;

import com.sjw.doran.memberservice.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CachedBasket {

    private Long id;
    private List<CachedBasketItem> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CachedBasketItem {

        private String itemUuid;
        private String itemName;
        private String itemImageUrl;
        private Category category;
        private int price;
        private int count;
    }
}
