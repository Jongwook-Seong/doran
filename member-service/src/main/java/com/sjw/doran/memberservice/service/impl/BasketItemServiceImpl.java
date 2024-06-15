package com.sjw.doran.memberservice.service.impl;

import com.sjw.doran.memberservice.client.ResilientItemServiceClient;
import com.sjw.doran.memberservice.dto.BasketItemDto;
import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.client.ItemServiceClient;
import com.sjw.doran.memberservice.mapper.BasketItemMapper;
import com.sjw.doran.memberservice.repository.BasketItemRepository;
import com.sjw.doran.memberservice.service.BasketItemService;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import com.sjw.doran.memberservice.vo.response.item.ItemSimpleResponse;
import com.sjw.doran.memberservice.vo.response.item.ItemSimpleWithCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
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
    private final ResilientItemServiceClient resilientItemServiceClient;
    private final BasketItemMapper basketItemMapper;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    @Transactional(readOnly = true)
    public List<ItemSimpleWithCountResponse> findAllByBasket(Basket basket) throws InterruptedException {
        List<String> itemUuidList = new ArrayList<>();
        List<BasketItem> basketItemList = basketItemRepository.findAllByBasket(basket);
        basketItemList.forEach(basketItem -> itemUuidList.add(basketItem.getItemUuid()));
//        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("MS-findAllByBasket-circuitbreaker");
//        List<ItemSimpleResponse> itemSimpleResponseList = circuitBreaker.run(() ->
//                itemServiceClient.getBookBasket(itemUuidList), throwable -> new ArrayList<>());
        List<ItemSimpleResponse> itemSimpleResponseList = resilientItemServiceClient.getBookBasket(itemUuidList);
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
        BasketItemDto basketItemDto = basketItemMapper.toBasketItemDto(request);
        BasketItem basketItem = basketItemMapper.toBasketItem(basketItemDto, basket);
        basketItemRepository.save(basketItem);
    }

    @Override
    @Transactional
    public void deleteBasketItem(Basket basket, String itemUuid) {
        basketItemRepository.deleteByBasketAndItemUuid(basket, itemUuid);
    }
}
