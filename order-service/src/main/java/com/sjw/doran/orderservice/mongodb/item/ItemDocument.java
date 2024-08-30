package com.sjw.doran.orderservice.mongodb.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "itemInfoForOrderService")
@Data
@AllArgsConstructor
public class ItemDocument {

    @Id
    private Long id;
    private String itemUuid;
    private String itemName;
    private String itemImageUrl;
}
