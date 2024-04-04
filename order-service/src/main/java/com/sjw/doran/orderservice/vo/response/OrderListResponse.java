package com.sjw.doran.orderservice.vo.response;

import com.sjw.doran.orderservice.vo.OrderSimple;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderListResponse {

    private List<OrderSimple> orderSimpleList;

    public static OrderListResponse getInstance(List<OrderSimple> orderSimpleList) {
        return OrderListResponse.builder()
                .orderSimpleList(orderSimpleList)
                .build();
    }
}
