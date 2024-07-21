package com.sjw.doran.memberservice.mapper;

import com.sjw.doran.memberservice.dto.BasketItemDto;
import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.kafka.basket.BasketTopicMessage;
import com.sjw.doran.memberservice.mongodb.BasketDocument;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BasketItemMapper {

    BasketItemMapper INSTANCE = Mappers.getMapper(BasketItemMapper.class);

    @Mapping(target = "basket", source = "basket")
    BasketItem toBasketItem(BasketItemDto basketItemDto, Basket basket);
    BasketItemDto toBasketItemDto(BasketItemCreateRequest request);

    @Mapping(target = "itemUuid", source = "basketItem.itemUuid")
    @Mapping(target = "count", source = "basketItem.count")
    BasketTopicMessage.BasketItemData toBasketItemData(BasketItem basketItem);

    List<BasketTopicMessage.BasketItemData> toBasketItemDataList(List<BasketItem> basketItems);

    @Mapping(target = "itemUuid", source = "basketItemData.itemUuid")
    @Mapping(target = "count", source = "basketItemData.count")
    BasketDocument.Item toBasketDocumentItem(BasketTopicMessage.BasketItemData basketItemData);

    List<BasketDocument.Item> toBasketDocumentItemListFromTopicMessageList(List<BasketTopicMessage.BasketItemData> basketItemDataList);

    @Mapping(target = "itemUuid", source = "basketItem.itemUuid")
    @Mapping(target = "count", source = "basketItem.count")
    BasketDocument.Item toBasketDocumentItem(BasketItem basketItem);

    List<BasketDocument.Item> toBasketDocumentItemListFromBasketItemList(List<BasketItem> basketItems);

    @Mapping(target = "basket", source = "basket")
    @Mapping(target = "itemUuid", source = "basketDocumentItem.itemUuid")
    @Mapping(target = "count", source = "basketDocumentItem.count")
    BasketItem toBasketItem(Basket basket, BasketDocument.Item basketDocumentItem);

//    List<BasketItem> toBasketItemList(Basket basket, List<BasketDocument.Item> basketDocumentItems);
}
