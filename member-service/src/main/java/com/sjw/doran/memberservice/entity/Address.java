package com.sjw.doran.memberservice.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Address {

    private String roadName;
    private String detailedAddr;
    private String zipcode;

    public static Address getInstance(String roadName, String detailedAddr, String zipcode) {
        return Address.builder()
                .roadName(roadName)
                .detailedAddr(detailedAddr)
                .zipcode(zipcode)
                .build();
    }
}
