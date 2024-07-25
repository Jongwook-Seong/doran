package com.sjw.doran.memberservice.redis;

import com.sjw.doran.memberservice.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CachedBasket {

    @Id
    private String userUuid;
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
