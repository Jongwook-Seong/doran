package com.sjw.doran.requestdatagenerator.member.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sjw.doran.requestdatagenerator.common.CustomObjectMapper;
import com.sjw.doran.requestdatagenerator.common.Generator;
import com.sjw.doran.requestdatagenerator.item.entity.Book;
import com.sjw.doran.requestdatagenerator.item.entity.Category;
import com.sjw.doran.requestdatagenerator.item.repository.ItemRepository;
import com.sjw.doran.requestdatagenerator.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberRequestGenerateService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final Generator generator;
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberCreateRequest {
        private String userId;
        private String password;
        private String username;
        private Date birthDate;
        private String email;
        private String phoneNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasketItemAddRequest {
        private String itemUuid;
        private int count;
    }

    public Map<Long, MemberCreateRequest> createMemberCreateRequests(long size) {
        Map<Long, MemberCreateRequest> requestMap = new HashMap<>();
        for (long i = 1; i <= size; i++) {
            requestMap.put(i, generateMemberCreateRequest());
        }
        return requestMap;
    }

    public Map<Long, BasketItemAddRequest> createBasketItemAddRequests(long size) {
        Map<Long, BasketItemAddRequest> requestMap = new HashMap<>();
        for (long i = 1; i <= size; i++) {
            requestMap.put(i, generateBasketItemAddRequest());
        }
        return requestMap;
    }

    private MemberCreateRequest generateMemberCreateRequest() {
        String userId = generator.generateRandomString(20);
        String password = generator.generateRandomString(20);
        String username = generator.generateRandomString(20);
        Date birthDate = new Date(
                generator.generateRandomInteger(2023),
                generator.generateRandomInteger(12),
                generator.generateRandomInteger(28));
        String email = generator.generateRandomString(20);
        String phoneNumber = generator.generateRandomString(20);
        return new MemberCreateRequest(userId, password, username, birthDate, email, phoneNumber);
    }

    private BasketItemAddRequest generateBasketItemAddRequest() {
        Book book = itemRepository.findAnyByCategory(Category.BOOK).orElse(null);
        String itemUuid = book.getItemUuid();
        int count = generator.generateRandomInteger(3);
        if (count <= 0) count = 1;
        return new BasketItemAddRequest(itemUuid, count);
    }

    public void createHeaderValueJsonFile() {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        List<String> allUserUuid = memberRepository.findAllUserUuid();
        for (String userUuid : allUserUuid) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("userUuid", userUuid);
            arrayNode.add(node);
        }
        try {
            objectMapper.writeValue(new File("userUuids.json"), arrayNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
