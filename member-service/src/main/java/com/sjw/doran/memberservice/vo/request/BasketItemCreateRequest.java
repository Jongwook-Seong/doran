package com.sjw.doran.memberservice.vo.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BasketItemCreateRequest {

    private String itemUuid;
    private int count;
}
