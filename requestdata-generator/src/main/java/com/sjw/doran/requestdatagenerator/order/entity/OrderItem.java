package com.sjw.doran.requestdatagenerator.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "item_uuid")
    private String itemUuid;

    @Column(name = "order_price")
    private int orderPrice;
    private int count;

    @Builder
    public OrderItem(Order order, String itemUuid, int orderPrice, int count) {
        this.order = order;
        this.itemUuid = itemUuid;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public void createOrder(Order order) {
        this.order = order;
    }
}
