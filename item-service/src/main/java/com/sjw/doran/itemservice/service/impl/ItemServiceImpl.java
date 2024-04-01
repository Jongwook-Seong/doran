package com.sjw.doran.itemservice.service.impl;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.dto.ItemDto;
import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.repository.ItemRepository;
import com.sjw.doran.itemservice.service.ItemService;
import com.sjw.doran.itemservice.vo.response.ItemSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Override
    public void saveBook(BookDto bookDto) {
        Item item = modelMapper.map(bookDto, Book.class);
        itemRepository.save(item);
    }

    @Override
    public void deleteItem(String itemUuid) {
        itemRepository.deleteByItemUuid(itemUuid);
    }

    @Override
    public Item getItemDetail(String itemUuid) {
        return itemRepository.findByItemUuid(itemUuid)
                .orElseThrow(RuntimeException::new);
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
