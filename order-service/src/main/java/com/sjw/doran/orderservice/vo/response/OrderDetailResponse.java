package com.sjw.doran.orderservice.vo.response;

import com.sjw.doran.orderservice.entity.Address;
import com.sjw.doran.orderservice.entity.DeliveryStatus;
import com.sjw.doran.orderservice.entity.TransceiverInfo;
import com.sjw.doran.orderservice.vo.OrderItemSimple;
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
