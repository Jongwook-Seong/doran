package com.sjw.doran.memberservice.mapper;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.kafka.basket.BasketTopicMessage;
import com.sjw.doran.memberservice.kafka.common.BasketOperationType;
import com.sjw.doran.memberservice.mongodb.BasketDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = { BasketItemMapper.class })
public interface BasketMapper {

    BasketMapper INSTANCE = Mappers.getMapper(BasketMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "basket.id"),
            @Mapping(target = "operationType", source = "operationType"),
            @Mapping(target = "payload.id", source = "basket.id"),
            @Mapping(target = "payload.basketItems", source = "basketItems")
    })
    BasketTopicMessage toBasketTopicMessage(Basket basket, List<BasketItem> basketItems, BasketOperationType operationType);

    @Mappings({
            @Mapping(target = "id", source = "payload.id"),
            @Mapping(target = "items", source = "payload.basketItems")
    })
    BasketDocument toBasketDocument(BasketTopicMessage.Payload payload);
}
