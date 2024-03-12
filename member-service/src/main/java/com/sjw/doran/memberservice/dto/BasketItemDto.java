package com.sjw.doran.memberservice.dto;

import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import lombok.*;

@Data
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class BasketItemDto {

    private String itemUuid;
    private int count;

    public static BasketItemDto getInstanceForCreate(BasketItemCreateRequest basketItemCreateRequest) {
        return BasketItemDto.builder()
                .itemUuid(basketItemCreateRequest.getItemUuid())
                .count(basketItemCreateRequest.getCount())
                .build();
    }
}
