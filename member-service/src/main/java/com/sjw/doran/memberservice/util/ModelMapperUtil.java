package com.sjw.doran.memberservice.util;

import com.sjw.doran.memberservice.dto.BasketItemDto;
import com.sjw.doran.memberservice.entity.BasketItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@RequiredArgsConstructor
public class ModelMapperUtil {

    private final ModelMapper mapper;

    public BasketItem BasketItemDtoConvertToEntity(BasketItemDto basketItemDto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        BasketItem basketItem = mapper.map(basketItemDto, BasketItem.class);
        return basketItem;
    }
}
