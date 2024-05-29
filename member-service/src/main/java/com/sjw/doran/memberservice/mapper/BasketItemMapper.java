package com.sjw.doran.memberservice.mapper;

import com.sjw.doran.memberservice.dto.BasketItemDto;
import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BasketItemMapper {

    BasketItemMapper INSTANCE = Mappers.getMapper(BasketItemMapper.class);

    @Mapping(target = "basket", source = "basket")
    BasketItem toBasketItem(BasketItemDto basketItemDto, Basket basket);
    BasketItemDto toBasketItemDto(BasketItemCreateRequest request);
}
