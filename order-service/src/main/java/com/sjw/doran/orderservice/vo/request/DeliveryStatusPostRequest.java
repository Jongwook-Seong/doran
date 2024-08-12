package com.sjw.doran.orderservice.vo.request;

import com.sjw.doran.orderservice.entity.DeliveryStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatusPostRequest {

    private String courier;
    private String contactNumber;
    private String postLocation;
    private DeliveryStatus deliveryStatus;
}
