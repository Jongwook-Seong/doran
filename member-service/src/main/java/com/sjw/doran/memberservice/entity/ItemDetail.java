package com.sjw.doran.memberservice.entity;

import jakarta.persistence.*;

@Entity
public class ItemDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_detail_id")
    private Long id;
    private String itemUuid;
    private int count;
}
