package com.sjw.doran.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryTracking {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_tracking_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private String courier;
    private String contactNumber;
    private String postLocation;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime postDateTime;

    @Builder
    public DeliveryTracking(Delivery delivery, String courier, String contactNumber, String postLocation) {
        this.delivery = delivery;
        this.courier = courier;
        this.contactNumber = contactNumber;
        this.postLocation = postLocation;
        this.postDateTime = LocalDateTime.now();
    }
}
