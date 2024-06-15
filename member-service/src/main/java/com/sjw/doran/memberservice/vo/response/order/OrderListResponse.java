package com.sjw.doran.memberservice.vo.response.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponse {

    private List<OrderSimple> orderSimpleList;

    public static OrderListResponse getInstance(List<OrderSimple> orderSimpleList) {
        return OrderListResponse.builder()
                .orderSimpleList(orderSimpleList)
                .build();
    }
}