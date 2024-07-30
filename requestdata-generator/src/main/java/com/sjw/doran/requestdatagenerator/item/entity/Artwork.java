package com.sjw.doran.requestdatagenerator.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("ARTWORK")
public class Artwork extends Item {

    private String artist;
    private String explanation;
    @Column(name = "work_size")
    private String workSize;
    @Column(name = "production_year")
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
