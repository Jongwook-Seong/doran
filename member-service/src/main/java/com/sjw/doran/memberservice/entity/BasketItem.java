package com.sjw.doran.memberservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_basket_id", columnList = "basket_id"))
public class BasketItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    @Column(unique = true)
    private String itemUuid;
    private int count;

    @Builder
    public BasketItem(Basket basket, String itemUuid, int count) {
        this.basket = basket;
        this.itemUuid = itemUuid;
        this.count = count;
    }
}
