package com.sjw.doran.memberservice.vo.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BasketItemResponse {

    String itemUuid;
    String message;

    public static BasketItemResponse getInstance(String itemUuid, String message) {
        return BasketItemResponse.builder()
                .itemUuid(itemUuid)
                .message(message)
                .build();
    }
}
