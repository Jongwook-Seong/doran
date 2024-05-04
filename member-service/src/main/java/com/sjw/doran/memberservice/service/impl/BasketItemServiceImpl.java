package com.sjw.doran.memberservice.service.impl;

import com.sjw.doran.memberservice.dto.BasketItemDto;
import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.feign.ItemServiceClient;
import com.sjw.doran.memberservice.repository.BasketItemRepository;
import com.sjw.doran.memberservice.service.BasketItemService;
import com.sjw.doran.memberservice.util.ModelMapperUtil;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import com.sjw.doran.memberservice.vo.request.ItemListRequest;
import com.sjw.doran.memberservice.vo.response.ItemSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketItemServiceImpl implements BasketItemService {

    private final BasketItemRepository basketItemRepository;
    private final ItemServiceClient itemServiceClient;
    private final ModelMapperUtil modelMapperUtil;

    @Override
    @Transactional(readOnly = true)
    public List<ItemSimpleResponse> findAllByBasket(Basket basket) {
        List<String> itemUuidList = new ArrayList<>();
        List<BasketItem> basketItemList = basketItemRepository.findAllByBasket(basket);
        basketItemList.forEach(basketItem -> itemUuidList.add(basketItem.getItemUuid()));
        ItemListRequest itemListRequest = new ItemListRequest(itemUuidList);
        List<ItemSimpleResponse> bookBasket = itemServiceClient.getBookBasket(itemListRequest);
        return bookBasket;
    }

    @Override
    @Transactional
    public void addBasketItem(Basket basket, BasketItemCreateRequest basketItemCreateRequest) {
        BasketItemDto basketItemDto = BasketItemDto.getInstanceForCreate(basketItemCreateRequest);
        BasketItem basketItem = modelMapperUtil.BasketItemDtoConvertToEntity(basketItemDto);
        basketItem.setBasket(basket);
        basketItemRepository.save(basketItem);
    }

    @Override
    @Transactional
    public void deleteBasketItem(Basket basket, String itemUuid) {
        basketItemRepository.deleteByBasketAndItemUuid(basket, itemUuid);
    }
}
