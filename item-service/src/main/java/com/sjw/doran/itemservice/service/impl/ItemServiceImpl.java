package com.sjw.doran.itemservice.service.impl;

import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.repository.ItemRepository;
import com.sjw.doran.itemservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public Item getItemDetail(String itemUuid) {

        Item findItem = itemRepository.findByItemUuid(itemUuid)
                .orElseThrow(RuntimeException::new);

        return findItem;
    }
}
