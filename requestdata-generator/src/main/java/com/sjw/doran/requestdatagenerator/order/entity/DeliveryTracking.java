package com.sjw.doran.requestdatagenerator.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @Column(name = "contact_number")
    private String contactNumber;
    @Column(name = "post_location")
    private String postLocation;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "post_date_time")
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
