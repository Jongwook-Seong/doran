package com.sjw.doran.memberservice.service.impl;

import com.sjw.doran.memberservice.dto.BasketItemDto;
import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.client.ItemServiceClient;
import com.sjw.doran.memberservice.mapper.BasketItemMapper;
import com.sjw.doran.memberservice.repository.BasketItemRepository;
import com.sjw.doran.memberservice.service.BasketItemService;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import com.sjw.doran.memberservice.vo.response.ItemSimpleResponse;
import com.sjw.doran.memberservice.vo.response.ItemSimpleWithCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasketItemServiceImpl implements BasketItemService {

    private final BasketItemRepository basketItemRepository;
    private final ItemServiceClient itemServiceClient;
    private final ModelMapperUtil modelMapperUtil;
    private final BasketItemMapper basketItemMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ItemSimpleWithCountResponse> findAllByBasket(Basket basket) {
        List<String> itemUuidList = new ArrayList<>();
        List<BasketItem> basketItemList = basketItemRepository.findAllByBasket(basket);
        basketItemList.forEach(basketItem -> itemUuidList.add(basketItem.getItemUuid()));
        List<ItemSimpleResponse> itemSimpleResponseList = itemServiceClient.getBookBasket(itemUuidList);
        return getItemSimpleWithCountResponseList(basketItemList, itemSimpleResponseList);
    }

    private static List<ItemSimpleWithCountResponse> getItemSimpleWithCountResponseList(List<BasketItem> basketItemList, List<ItemSimpleResponse> itemSimpleResponseList) {
        Map<String, BasketItem> basketItemMap = basketItemList.stream()
                .collect(Collectors.toMap(BasketItem::getItemUuid, item -> item));
        return itemSimpleResponseList.stream()
                .map(itemSimpleResponse -> {
                    BasketItem basketItem = basketItemMap.get(itemSimpleResponse.getItemUuid());
                    return ItemSimpleWithCountResponse.getInstance(itemSimpleResponse, basketItem.getCount());
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addBasketItem(Basket basket, BasketItemCreateRequest request) {
//        BasketItemDto basketItemDto = BasketItemDto.getInstanceForCreate(request);
        BasketItemDto basketItemDto = basketItemMapper.toBasketItemDto(request);
//        BasketItem basketItem = modelMapperUtil.BasketItemDtoConvertToEntity(basketItemDto);
        BasketItem basketItem = basketItemMapper.toBasketItem(basketItemDto, basket);
//        basketItem.setBasket(basket);
        basketItemRepository.save(basketItem);
    }

    @Override
    @Transactional
    public void deleteBasketItem(Basket basket, String itemUuid) {
        basketItemRepository.deleteByBasketAndItemUuid(basket, itemUuid);
    }
}
