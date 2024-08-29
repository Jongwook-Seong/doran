package com.sjw.doran.memberservice.service.impl;

import com.sjw.doran.memberservice.client.ResilientItemServiceClient;
import com.sjw.doran.memberservice.dto.BasketItemDto;
import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.kafka.basket.BasketEvent;
import com.sjw.doran.memberservice.kafka.basket.BasketTopicMessage;
import com.sjw.doran.memberservice.kafka.common.BasketOperationType;
import com.sjw.doran.memberservice.mapper.BasketItemMapper;
import com.sjw.doran.memberservice.mapper.BasketMapper;
import com.sjw.doran.memberservice.mapper.item.ItemMapper;
import com.sjw.doran.memberservice.mongodb.basket.BasketDocument;
import com.sjw.doran.memberservice.mongodb.basket.BasketDocumentRepository;
import com.sjw.doran.memberservice.mongodb.item.ItemDocument;
import com.sjw.doran.memberservice.mongodb.item.ItemDocumentRepository;
import com.sjw.doran.memberservice.redis.service.BasketItemListCacheService;
import com.sjw.doran.memberservice.repository.BasketItemRepository;
import com.sjw.doran.memberservice.service.BasketItemService;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import com.sjw.doran.memberservice.vo.response.item.ItemSimpleResponse;
import com.sjw.doran.memberservice.vo.response.item.ItemSimpleWithCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final BasketDocumentRepository basketDocumentRepository;
    private final BasketItemListCacheService basketItemListCacheService;
    private final ItemDocumentRepository itemDocumentRepository;
    private final ResilientItemServiceClient resilientItemServiceClient;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final BasketMapper basketMapper;
    private final BasketItemMapper basketItemMapper;
    private final ItemMapper itemMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ItemSimpleWithCountResponse> findAllByBasket(Basket basket) throws InterruptedException {
        List<String> itemUuidList = new ArrayList<>();
//        List<BasketItem> basketItemList = basketItemRepository.findAllByBasket(basket);
//        basketItemList.forEach(basketItem -> itemUuidList.add(basketItem.getItemUuid()));
        BasketDocument basketDocument = basketDocumentRepository.findById(basket.getId()).get();
        basketDocument.setItems(new ArrayList<>());
        /* Set cache */
        basketItemListCacheService.set(basketMapper.toCachedBasket(basketDocument));
        basketDocument.getItems().forEach(item -> itemUuidList.add(item.getItemUuid()));
        List<BasketItem> basketItemList = new ArrayList<>();
        basketDocument.getItems().forEach(item -> basketItemList.add(basketItemMapper.toBasketItem(basket, item)));
        // Get Item-Service data by Kafka Asynchronous Communication, instead of FeignClient
        List<ItemDocument> itemDocuments = itemDocumentRepository.findAllByItemUuidIn(itemUuidList);
        return getItemSimpleWithCountResponseList(basketItemList, itemMapper.toItemSimpleResponseList(itemDocuments));
//        List<ItemSimpleResponse> itemSimpleResponseList = resilientItemServiceClient.getBookBasket(itemUuidList);
//        return getItemSimpleWithCountResponseList(basketItemList, itemSimpleResponseList);
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
    public void addBasketItem(Basket basket, String userUuid, BasketItemCreateRequest request) throws InterruptedException {
        BasketItemDto basketItemDto = basketItemMapper.toBasketItemDto(request);
        BasketItem basketItem = basketItemMapper.toBasketItem(basketItemDto, basket);
        basketItemRepository.save(basketItem);
        // Get Item-Service data by Kafka Asynchronous Communication, instead of FeignClient
        ItemDocument itemDocument = itemDocumentRepository.findAllByItemUuidIn(List.of(basketItem.getItemUuid())).get(0);
        /* Mapping */
        BasketTopicMessage.BasketItemData basketItemData =
                basketItemMapper.toBasketTopicMessageBasketItemDataFromItemSimpleResponse(
                        itemMapper.toItemSimpleResponse(itemDocument), basketItem.getCount());
//        /* Get additional fields of BasketItem from FeignClient */
//        ItemSimpleResponse itemSimpleResponse = resilientItemServiceClient.getBookBasket(List.of(basketItem.getItemUuid())).get(0);
//        /* Mapping */
//        BasketTopicMessage.BasketItemData basketItemData =
//                basketItemMapper.toBasketTopicMessageBasketItemDataFromItemSimpleResponse(itemSimpleResponse, basketItem.getCount());
        BasketTopicMessage basketTopicMessage = basketMapper.toBasketTopicMessage(basket, new ArrayList<>(), userUuid, null);
        basketTopicMessage.getPayload().getBasketItems().add(basketItemData);
        /* Publish kafka message */
        applicationEventPublisher.publishEvent(new BasketEvent(this, basket.getId(), basketTopicMessage.getPayload(), BasketOperationType.ADD_ITEM));
    }

    @Override
    @Transactional
    public void deleteBasketItem(Basket basket, String userUuid, String itemUuid) {
        basketItemRepository.deleteByBasketAndItemUuid(basket, itemUuid);
        /* Publish kafka message */
        BasketTopicMessage.BasketItemData  basketItemData = new BasketTopicMessage.BasketItemData(itemUuid, 0);
        BasketTopicMessage.Payload payload = new BasketTopicMessage.Payload(basket.getId(), userUuid, List.of(basketItemData));
        applicationEventPublisher.publishEvent(new BasketEvent(this, basket.getId(), payload, BasketOperationType.REMOVE_ITEM));
    }
}
