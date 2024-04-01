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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Slice<ItemSimpleResponse> getItemSimpleSlice(List<String> itemUuidList, Pageable pageable) {
//        Slice<ItemSimpleResponse> itemSimpleSlice = itemRepository.findByItemUuidList(itemUuidList, pageable)
//                .map(item -> modelMapper.map(item, ItemSimpleResponse.class));
        Slice<ItemSimpleResponse> itemSimpleSlice = itemRepository.findByItemUuidList(itemUuidList, pageable)
                .map(item -> item != null ? modelMapper.map(item, ItemSimpleResponse.class) : null);
        return itemSimpleSlice;
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
