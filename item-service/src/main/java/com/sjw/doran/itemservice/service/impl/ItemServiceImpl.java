package com.sjw.doran.itemservice.service.impl;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.dto.ItemDto;
import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.repository.ItemRepository;
import com.sjw.doran.itemservice.service.ItemService;
import com.sjw.doran.itemservice.util.MessageUtil;
import com.sjw.doran.itemservice.vo.response.ItemSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final MessageUtil messageUtil;

    @Override
    @Transactional
    public void saveBook(BookDto bookDto) {
        Item item = modelMapper.map(bookDto, Book.class);
        try {
            itemRepository.save(item);
        } catch (Exception e) {
            throw new RuntimeException(messageUtil.getItemCreateErrorMessage());
        }
    }

    @Override
    @Transactional
    public void deleteItem(String itemUuid) {
        try {
            itemRepository.deleteByItemUuid(itemUuid);
        } catch (Exception e) {
            throw new RuntimeException(messageUtil.getItemDeleteErrorMessage());
        }
    }

    @Override
    public Item getItemDetail(String itemUuid) {
        return itemRepository.findByItemUuid(itemUuid).orElseThrow(() -> {
            throw new NoSuchElementException(messageUtil.getNoSuchElementItemUuidErrorMessage(itemUuid)); });
    }

    @Override
    public List<ItemSimpleResponse> getItemSimpleList(List<String> itemUuidList) {
        List<Item> items = itemRepository.findByItemUuidList(itemUuidList);
        List<ItemSimpleResponse> itemSimpleResponseList = new ArrayList<>();
        for (Item item : items) {
            ItemDto itemDto = modelMapper.map(item, ItemDto.class);
            itemSimpleResponseList.add(ItemSimpleResponse.getInstanceAsItem(itemDto));
        }
        return itemSimpleResponseList;
    }

    @Override
    public List<ItemSimpleResponse> getBooksByKeyword(String keyword) {

        List<Book> books = itemRepository.findBookByKeyword(keyword);
        List<ItemSimpleResponse> itemSimpleResponseList = new ArrayList<>();
        for (Book book : books) {
            BookDto bookDto = modelMapper.map(book, BookDto.class);
            itemSimpleResponseList.add(ItemSimpleResponse.getInstanceAsBook(bookDto));
        }
        return itemSimpleResponseList;
    }
}
