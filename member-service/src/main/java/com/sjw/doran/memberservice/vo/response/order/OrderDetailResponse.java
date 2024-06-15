package com.sjw.doran.memberservice.vo.response.order;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {

    private List<OrderItemSimple> orderItemSimpleList;
    private LocalDateTime orderDate;
    private DeliveryStatus deliveryStatus;
    private TransceiverInfo transceiverInfo;
    private Address address;

    public static OrderDetailResponse getInstance(List<OrderItemSimple> orderItemSimpleList,
                                                  LocalDateTime orderDate,
                                                  DeliveryStatus deliveryStatus,
                                                  TransceiverInfo transceiverInfo,
                                                  Address address) {
        return OrderDetailResponse.builder()
                .orderItemSimpleList(orderItemSimpleList)
                .orderDate(orderDate)
                .deliveryStatus(deliveryStatus)
                .transceiverInfo(transceiverInfo)
                .address(address)
                .build();
    }
}