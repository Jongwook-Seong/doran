package com.sjw.doran.orderservice.dto;

import com.sjw.doran.orderservice.entity.DeliveryStatus;
import lombok.*;

@Data
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {

    private DeliveryStatus deliveryStatus;

    public static DeliveryDto getInstanceForCreate() {
        return DeliveryDto.builder()
                .deliveryStatus(DeliveryStatus.READY)
                .build();
    }
}
