package com.sjw.doran.orderservice.vo;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTrackingInfo {

    private String courier;
    private String contactNumber;
    private String postLocation;
    private LocalDateTime postDateTime;

    public static DeliveryTrackingInfo getInstance(String courier, String contactNumber, String postLocation, LocalDateTime postDateTime) {
        return DeliveryTrackingInfo.builder()
                .courier(courier)
                .contactNumber(contactNumber)
                .postLocation(postLocation)
                .postDateTime(postDateTime)
                .build();
    }
}
