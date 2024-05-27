package com.sjw.doran.itemservice.service.impl;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.dto.ItemDto;
import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.entity.Category;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.mapper.BookMapper;
import com.sjw.doran.itemservice.mapper.ItemMapper;
import com.sjw.doran.itemservice.repository.ItemRepository;
import com.sjw.doran.itemservice.service.AwsS3UploadService;
import com.sjw.doran.itemservice.service.ItemService;
import com.sjw.doran.itemservice.util.MessageUtil;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import com.sjw.doran.itemservice.vo.response.ItemSimpleResponse;
import com.sjw.doran.itemservice.vo.response.ItemSimpleWithQuantityResponse;
import com.sjw.doran.itemservice.vo.response.ItemSimpleWithoutPriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final BookMapper bookMapper;
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
        BookDto bookDto = bookMapper.toBookDto(request, UUID.randomUUID().toString(), Category.BOOK);
        Item item = bookMapper.toBook(bookDto);
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
            ItemDto itemDto = itemMapper.toItemDto(item);
            ItemSimpleResponse itemSimpleResponse = itemMapper.toItemSimpleResponse(itemDto);
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
            ItemDto itemDto = itemMapper.toItemDto(item);
            ItemSimpleWithQuantityResponse itemSimpleWQResponse = itemMapper.toItemSimpleWQResponse(itemDto);
            itemSimpleWQResponseList.add(itemSimpleWQResponse);
        }
        return itemSimpleWQResponseList;
    }

    @Override
    public List<ItemSimpleWithoutPriceResponse> getItemSimpleWithoutPriceList(List<String> itemUuidList) {
        List<Item> itemList = itemRepository.findByItemUuidList(itemUuidList);
        List<ItemSimpleWithoutPriceResponse> itemSimpleWxPResponseList = new ArrayList<>();
        for (Item item : itemList) {
            if (item == null) continue;
            ItemDto itemDto = itemMapper.toItemDto(item);
            itemSimpleWxPResponseList.add(itemMapper.toItemSimpleWxPResponse(itemDto));
        }
        return itemSimpleWxPResponseList;
    }

    @Override
    public List<ItemSimpleResponse> getBooksByKeyword(String keyword) {

        List<Book> books = itemRepository.findBookByKeyword(keyword);
        List<ItemSimpleResponse> itemSimpleResponseList = new ArrayList<>();
        for (Book book : books) {
            BookDto bookDto = bookMapper.toBookDto(book);
            itemSimpleResponseList.add(bookMapper.toItemSimpleResponse(bookDto));
        }
        return itemSimpleResponseList;
    }

    @Override
    @Transactional
    public void subtractItems(List<String> itemUuidList, List<Integer> countList) {
        itemRepository.updateStockQuantity(itemUuidList, countList);
    }

    @Override
    @Transactional
    public void restoreItems(List<String> itemUuidList, List<Integer> countList) {
        countList.replaceAll(count -> -count);
        itemRepository.updateStockQuantity(itemUuidList, countList);
    }
}
