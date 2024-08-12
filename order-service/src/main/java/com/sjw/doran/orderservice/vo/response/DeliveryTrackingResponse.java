package com.sjw.doran.orderservice.vo.response;

import com.sjw.doran.orderservice.entity.DeliveryStatus;
import com.sjw.doran.orderservice.vo.DeliveryTrackingInfo;
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
