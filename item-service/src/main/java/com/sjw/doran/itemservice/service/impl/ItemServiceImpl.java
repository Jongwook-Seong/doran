package com.sjw.doran.itemservice.service.impl;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.dto.ItemDto;
import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.repository.ItemRepository;
import com.sjw.doran.itemservice.service.AwsS3UploadService;
import com.sjw.doran.itemservice.service.ItemService;
import com.sjw.doran.itemservice.util.MessageUtil;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import com.sjw.doran.itemservice.vo.response.ItemSimpleResponse;
import com.sjw.doran.itemservice.vo.response.ItemSimpleWithQuantityResponse;
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
//    private final AwsS3UploadService awsS3UploadService;

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
    @Transactional
    public void saveBook(BookCreateRequest request) {
        BookDto bookDto = BookDto.getInstanceForCreate(request);
        Item item = modelMapper.map(bookDto, Book.class);
//        String itemImageUrl = awsS3UploadService.saveFile(request.getFileData(), item.getItemUuid());
//        item.setItemImageUrl(itemImageUrl);
        try {
            itemRepository.save(item);
        } catch (Exception e) {
            throw new RuntimeException(messageUtil.getItemCreateErrorMessage());
        }
    }

    @Override
    public Item getItemDetail(String itemUuid) {
        return itemRepository.findByItemUuid(itemUuid).orElseThrow(() -> {
            throw new NoSuchElementException(messageUtil.getNoSuchElementItemUuidErrorMessage(itemUuid)); });
    }

    @Override
    public List<ItemSimpleResponse> getItemSimpleList(List<String> itemUuidList) {
        List<Item> itemList = itemRepository.findByItemUuidList(itemUuidList);
        List<ItemSimpleResponse> itemSimpleResponseList = new ArrayList<>();
        for (Item item : itemList) {
            if (item == null) continue;
            ItemSimpleResponse itemSimpleResponse = modelMapper.map(item, ItemSimpleResponse.class);
            itemSimpleResponseList.add(itemSimpleResponse);
        }
        return itemSimpleResponseList;
    }

    @Override
    public List<ItemSimpleWithQuantityResponse> getItemSimpleWithQuantityList(List<String> itemUuidList) {
        List<Item> itemList = itemRepository.findByItemUuidList(itemUuidList);
        ArrayList<ItemSimpleWithQuantityResponse> itemSimpleWQResponseList = new ArrayList<>();
        for (Item item : itemList) {
            if (item == null) continue;
            ItemSimpleWithQuantityResponse itemSimpleWQResponse = modelMapper.map(item, ItemSimpleWithQuantityResponse.class);
            itemSimpleWQResponseList.add(itemSimpleWQResponse);
        }
        return itemSimpleWQResponseList;
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
