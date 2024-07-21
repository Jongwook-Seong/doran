package com.sjw.doran.itemservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    private String itemUuid;
    private String itemName;
    private int price;
    private int stockQuantity;
    private String itemImageUrl;

    @Enumerated(EnumType.STRING)
    private Category category;

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            return;
        }
        this.stockQuantity = restStock;
    }

    public Item(String itemUuid, String itemName, int price, int stockQuantity, String itemImageUrl, Category category) {
        this.itemUuid = itemUuid;
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.itemImageUrl = itemImageUrl;
        this.category = category;
    }

    public Item(Long id, String itemUuid, String itemName, int price, int stockQuantity, String itemImageUrl, Category category) {
        this.id = id;
        this.itemUuid = itemUuid;
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.itemImageUrl = itemImageUrl;
        this.category = category;
    }
}
