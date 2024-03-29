package com.sjw.doran.itemservice.service;

import com.sjw.doran.itemservice.entity.Item;
import org.springframework.transaction.annotation.Transactional;

public interface ItemService {

    @Transactional
    void saveItem(Item item);

    @Transactional(readOnly = true)
    Item getItemDetail(String itemUuid);
}
