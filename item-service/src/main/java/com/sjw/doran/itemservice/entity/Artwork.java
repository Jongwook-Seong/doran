package com.sjw.doran.itemservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("ARTWORK")
public class Artwork extends Item {

    private String artist;
    private String explanation;
    private String workSize;
    private int productionYear;

    @Builder
    public Artwork(String itemUuid, String itemName, int price, int stockQuantity, String itemImageUrl, Category category,
                String artist, String explanation, String workSize, int productionYear) {
        super(itemUuid, itemName, price, stockQuantity, itemImageUrl, category);
        this.artist = artist;
        this.explanation = explanation;
        this.workSize = workSize;
        this.productionYear = productionYear;
    }

    @Builder
    public Artwork(Long id, String itemUuid, String itemName, int price, int stockQuantity, String itemImageUrl, Category category,
                   String artist, String explanation, String workSize, int productionYear) {
        super(id, itemUuid, itemName, price, stockQuantity, itemImageUrl, category);
        this.artist = artist;
        this.explanation = explanation;
        this.workSize = workSize;
        this.productionYear = productionYear;
    }
}
