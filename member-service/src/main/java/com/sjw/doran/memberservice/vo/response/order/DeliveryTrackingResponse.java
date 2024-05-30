package com.sjw.doran.memberservice.vo.response.order;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTrackingResponse {

    private List<DeliveryTrackingInfo> deliveryTrackingInfoList;
    private DeliveryStatus deliveryStatus;

    public static DeliveryTrackingResponse getInstance(List<DeliveryTrackingInfo> deliveryTrackingInfoList, DeliveryStatus deliveryStatus) {
        return DeliveryTrackingResponse.builder()
                .deliveryTrackingInfoList(deliveryTrackingInfoList)
                .deliveryStatus(deliveryStatus)
                .build();
    }
}
