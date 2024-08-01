package com.sjw.doran.memberservice.redis.data;

import com.sjw.doran.memberservice.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RedisHash(value = "basket_item_list:v1", timeToLive = 7200)
//@RedisHash(value = "basket_item_list:v1", timeToLive = 1800)
public class CachedBasket implements Serializable {

    @Id
    private String userUuid;
    private List<CachedBasketItem> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CachedBasketItem implements Serializable {

        private String itemUuid;
        private String itemName;
        private String itemImageUrl;
        private Category category;
        private int price;
        private int count;
    }
}
