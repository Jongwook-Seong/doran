package com.sjw.doran.itemservice.service.impl;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.dto.ItemDto;
import com.sjw.doran.itemservice.entity.Artwork;
import com.sjw.doran.itemservice.entity.Book;
import com.sjw.doran.itemservice.entity.Category;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.kafka.common.OperationType;
import com.sjw.doran.itemservice.kafka.item.ItemEvent;
import com.sjw.doran.itemservice.kafka.item.ItemTopicMessage;
import com.sjw.doran.itemservice.mapper.ArtworkMapper;
import com.sjw.doran.itemservice.mapper.BookMapper;
import com.sjw.doran.itemservice.mapper.ItemMapper;
import com.sjw.doran.itemservice.mongodb.ItemDocument;
import com.sjw.doran.itemservice.mongodb.ItemDocumentRepository;
import com.sjw.doran.itemservice.repository.ItemRepository;
import com.sjw.doran.itemservice.service.AwsS3UploadService;
import com.sjw.doran.itemservice.service.ItemService;
import com.sjw.doran.itemservice.util.MessageUtil;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import com.sjw.doran.itemservice.vo.response.ItemSimpleResponse;
import com.sjw.doran.itemservice.vo.response.ItemSimpleWithQuantityResponse;
import com.sjw.doran.itemservice.vo.response.ItemSimpleWithoutPriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemDocumentRepository itemDocumentRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ItemMapper itemMapper;
    private final BookMapper bookMapper;
    private final ArtworkMapper artworkMapper;
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
            Item savedItem = itemRepository.save(item);
            /* Publish kafka message */
            ItemTopicMessage message = bookMapper.toItemTopicMessage((Book)savedItem, null);
            applicationEventPublisher.publishEvent(new ItemEvent(this, message.getId(), message.getPayload(), OperationType.CREATE));
        } catch (Exception e) {
            throw new RuntimeException(messageUtil.getItemCreateErrorMessage());
        }
    }

    @Override
    public Item getItemDetail(String itemUuid) {
        ItemDocument itemDocument = itemDocumentRepository.findByItemUuid(itemUuid);
        if (itemDocument == null) {
            return itemRepository.findByItemUuid(itemUuid).orElseThrow(() -> {
                throw new NoSuchElementException(messageUtil.getNoSuchElementItemUuidErrorMessage(itemUuid)); });
        }
        if (itemDocument.getCategory() == Category.BOOK) {
            return bookMapper.toBook(itemDocument);
        } else if (itemDocument.getCategory() == Category.ARTWORK) {
//            return artworkMapper.toArtwork(itemDocument);
            return null;
        } else {
            return null;
        }
    }

    @Override
    public List<ItemSimpleResponse> getItemSimpleList(List<String> itemUuidList) {
        List<ItemDocument> itemDocumentList = itemDocumentRepository.findByItemUuidList(itemUuidList);
        return itemMapper.toItemSimpleResponseList(itemDocumentList);
    }

    @Override
    public List<ItemSimpleWithQuantityResponse> getItemSimpleWithQuantityList(List<String> itemUuidList) {
        List<ItemDocument> itemDocumentList = itemDocumentRepository.findByItemUuidList(itemUuidList);
        return itemMapper.toItemSimpleWQResponseList(itemDocumentList);
    }

    @Override
    public List<ItemSimpleWithoutPriceResponse> getItemSimpleWithoutPriceList(List<String> itemUuidList) {
        List<ItemDocument> itemDocumentList = itemDocumentRepository.findByItemUuidList(itemUuidList);
        return itemMapper.toItemSimpleWxPResponseList(itemDocumentList);
    }

    @Override
    public List<ItemSimpleResponse> getBooksByKeyword(String keyword) {
        List<ItemDocument> itemDocumentList = itemDocumentRepository.findBooksByKeyword(keyword);
        return itemMapper.toItemSimpleResponseList(itemDocumentList);
    }

    public List<ItemSimpleResponse> getItemsByKeyword(String keyword) {
        List<ItemDocument> itemDocumentList = itemDocumentRepository.findByKeyword(keyword);
        return itemMapper.toItemSimpleResponseList(itemDocumentList);
    }

    @Override
    @Transactional
    public void subtractItems(List<String> itemUuidList, List<Integer> countList) {
        List<Item> items = itemRepository.updateStockQuantity(itemUuidList, countList);
        /* Publish kafka message */
        for (Item item : items) {
            if (item.getCategory() == Category.BOOK) {
                ItemTopicMessage message = bookMapper.toItemTopicMessage((Book) item, null);
                applicationEventPublisher.publishEvent(new ItemEvent(this, message.getId(), message.getPayload(), OperationType.UPDATE));
            } else if (item.getCategory() == Category.ARTWORK) {
                ItemTopicMessage message = artworkMapper.toItemTopicMessage((Artwork) item, null);
                applicationEventPublisher.publishEvent(new ItemEvent(this, message.getId(), message.getPayload(), OperationType.UPDATE));
            }
        }
    }

    @Override
    @Transactional
    public void restoreItems(List<String> itemUuidList, List<Integer> countList) {
        countList.replaceAll(count -> -count);
        List<Item> items = itemRepository.updateStockQuantity(itemUuidList, countList);
        /* Publish kafka message */
        for (Item item : items) {
            if (item.getCategory() == Category.BOOK) {
                ItemTopicMessage message = bookMapper.toItemTopicMessage((Book) item, null);
                applicationEventPublisher.publishEvent(new ItemEvent(this, message.getId(), message.getPayload(), OperationType.UPDATE));
            } else if (item.getCategory() == Category.ARTWORK) {
                ItemTopicMessage message = artworkMapper.toItemTopicMessage((Artwork) item, null);
                applicationEventPublisher.publishEvent(new ItemEvent(this, message.getId(), message.getPayload(), OperationType.UPDATE));
            }
        }
    }
}
