package com.sjw.doran.memberservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ItemDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    private String itemUuid;
    private int count;

    public ItemDetail(Basket basket, String itemUuid, int count) {
        this.basket = basket;
        this.itemUuid = itemUuid;
        this.count = count;
    }
}
