package com.sjw.doran.memberservice.service.impl;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.BasketItem;
import com.sjw.doran.memberservice.repository.BasketItemRepository;
import com.sjw.doran.memberservice.service.BasketItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketItemServiceImpl implements BasketItemService {

    private final BasketItemRepository basketItemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BasketItem> findAllByBasket(Basket basket) {
        return basketItemRepository.findAllByBasket(basket);
    }

    @Override
    public void addBasketItem(Basket basket, String itemUuid) {
//        basketItemRepository.saveBasketItem()
    }
}
