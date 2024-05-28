package com.sjw.doran.memberservice.vo.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasketItemCreateRequest {

    private String itemUuid;
    private int count;
}
