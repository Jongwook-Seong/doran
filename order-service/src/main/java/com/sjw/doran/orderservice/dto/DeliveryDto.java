package com.sjw.doran.orderservice.dto;

import com.sjw.doran.orderservice.entity.Address;
import com.sjw.doran.orderservice.entity.DeliveryStatus;
import com.sjw.doran.orderservice.entity.TransceiverInfo;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {

    private DeliveryStatus deliveryStatus;
    private TransceiverInfo transceiverInfo;
    private Address address;

    public static DeliveryDto getInstanceForCreate(TransceiverInfo transceiverInfo, Address address) {
        return DeliveryDto.builder()
                .deliveryStatus(DeliveryStatus.READY)
                .transceiverInfo(transceiverInfo)
                .address(address)
                .build();
    }
}
