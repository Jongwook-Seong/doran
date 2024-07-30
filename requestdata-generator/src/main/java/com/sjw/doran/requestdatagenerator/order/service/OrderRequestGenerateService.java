package com.sjw.doran.requestdatagenerator.order.service;

import com.sjw.doran.requestdatagenerator.common.Generator;
import com.sjw.doran.requestdatagenerator.item.entity.Book;
import com.sjw.doran.requestdatagenerator.item.entity.Category;
import com.sjw.doran.requestdatagenerator.item.repository.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderRequestGenerateService {

    private final ItemRepository itemRepository;
    private final Generator generator;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderCreateRequest {
        private List<ItemSimpleInfo> itemSimpleInfoList;
        private TransceiverInfo transceiverInfo;
        private Address address;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemSimpleInfo {
        private String itemUuid;
        private int count;
        private int price;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransceiverInfo {
        private String ordererName;
        private String receiverName;
        private String receiverPhoneNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String city;
        private String street;
        private String details;
        private String zipcode;
    }

    public Map<Long, OrderCreateRequest> createOrderCreateRequest(long size) {
        Map<Long, OrderCreateRequest> requestMap = new HashMap<>();
        for (long i = 1; i <= size; i++) {
            requestMap.put(i, generateOrderCreateRequest());
        }
        return requestMap;
    }

    private OrderCreateRequest generateOrderCreateRequest() {
        int itemCount = generator.generateRandomInteger(6);
        if (itemCount <= 0) itemCount += 6;

        List<String> itemUuidList = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();
        List<ItemSimpleInfo> itemSimpleInfoList = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            Book book = itemRepository.findAnyByCategory(Category.BOOK).orElse(null);
            itemUuidList.add(book.getItemUuid());
            int count = generator.generateRandomInteger(10);
            if (count <= 0) count = 1;
            countList.add(count);
            itemSimpleInfoList.add(new ItemSimpleInfo(book.getItemUuid(), count, book.getPrice()));
        }

        String orderName = generator.generateRandomString(20);
        String receiverName = generator.generateRandomString(20);
        String receiverPhoneNumber = generator.generateRandomString(20);
        TransceiverInfo transceiverInfo = new TransceiverInfo(orderName, receiverName, receiverPhoneNumber);

        String city = generator.generateRandomString(20);
        String street = generator.generateRandomString(20);
        String details = generator.generateRandomString(20);
        String zipcode = generator.generateRandomString(20);
        Address address = new Address(city, street, details, zipcode);

        return new OrderCreateRequest(itemSimpleInfoList, transceiverInfo, address);
    }
}
