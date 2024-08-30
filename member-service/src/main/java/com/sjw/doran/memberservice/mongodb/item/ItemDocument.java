package com.sjw.doran.memberservice.mongodb.item;

import com.sjw.doran.memberservice.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "itemInfoForMemberService")
@Data
@AllArgsConstructor
public class ItemDocument {

    @Id
    private Long id;
    private String itemUuid;
    private String itemName;
    private String itemImageUrl;
    private Category category;
    private int price;
}
