package com.sjw.doran.requestdatagenerator.member.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Address {

    private String city;
    private String street;
    private String details;
    private String zipcode;

    public static Address getInstance(String city, String street, String details, String zipcode) {
        return Address.builder()
                .city(city)
                .street(street)
                .details(details)
                .zipcode(zipcode)
                .build();
    }
}
