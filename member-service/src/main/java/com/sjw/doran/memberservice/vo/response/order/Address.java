package com.sjw.doran.memberservice.vo.response.order;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
public class Address {

    private String city;
    private String street;
    private String details;
    private String zipcode;

    @Builder
    public Address(String city, String street, String details, String zipcode) {
        this.city = city;
        this.street = street;
        this.details = details;
        this.zipcode = zipcode;
    }
}
