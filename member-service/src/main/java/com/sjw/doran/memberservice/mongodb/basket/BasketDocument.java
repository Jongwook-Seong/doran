package com.sjw.doran.memberservice.mongodb.basket;

import com.sjw.doran.memberservice.entity.Category;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "basketItemList")
@Data
@AllArgsConstructor
public class BasketDocument {

    @Id
    private Long id;
    private String userUuid;
    private List<Item> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {

        private String itemUuid;
        private String itemName;
        private String itemImageUrl;
        private Category category;
        private int price;
        private int count;
    }
}
