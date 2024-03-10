package com.sjw.doran.memberservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class BasketItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    private String itemUuid;
    private int count;

    public BasketItem(Basket basket, String itemUuid, int count) {
        this.basket = basket;
        this.itemUuid = itemUuid;
        this.count = count;
    }
}
