package com.sjw.doran.memberservice.repository;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.ItemDetail;

import java.util.List;

public interface ItemDetailRepositoryCustom {

    List<ItemDetail> findAllByBasket(Basket basket);
}
