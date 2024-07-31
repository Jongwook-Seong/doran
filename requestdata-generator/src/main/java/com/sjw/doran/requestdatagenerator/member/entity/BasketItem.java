package com.sjw.doran.requestdatagenerator.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasketItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    @Column(name = "item_uuid", unique = true)
    private String itemUuid;
    private int count;

    @Builder
    public BasketItem(Basket basket, String itemUuid, int count) {
        this.basket = basket;
        this.itemUuid = itemUuid;
        this.count = count;
    }
}
