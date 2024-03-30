package com.sjw.doran.itemservice.service.impl;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.dto.ItemDto;
import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.repository.ItemRepository;
import com.sjw.doran.itemservice.service.ItemService;
import com.sjw.doran.itemservice.vo.request.ItemCreateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void saveBook(BookDto bookDto) {
        Item item = modelMapper.map(bookDto, Book.class);
        itemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public Item getItemDetail(String itemUuid) {

        System.out.println("ItemServiceImpl.getItemDetail");
        System.out.println("itemUuid = " + itemUuid);
        Item findItem = itemRepository.findByItemUuid(itemUuid)
                .orElseThrow(RuntimeException::new);
        System.out.println("findItem = " + findItem);

        return findItem;
    }
}
