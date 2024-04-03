package com.sjw.doran.orderservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTrackingDto {

    private String courier;
    private String contactNumber;
    private String postLocation;
    private LocalDateTime postDateTime;

    public static DeliveryTrackingDto getInstanceForCreate(String courier, String contactNumber, String postLocation) {
        return DeliveryTrackingDto.builder()
                .courier(courier)
                .contactNumber(contactNumber)
                .postLocation(postLocation)
                .postDateTime(LocalDateTime.now())
                .build();
    }
}
