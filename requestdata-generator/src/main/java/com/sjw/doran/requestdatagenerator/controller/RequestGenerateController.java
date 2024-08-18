package com.sjw.doran.requestdatagenerator.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sjw.doran.requestdatagenerator.common.CustomObjectMapper;
import com.sjw.doran.requestdatagenerator.common.Generator;
import com.sjw.doran.requestdatagenerator.item.entity.Item;
import com.sjw.doran.requestdatagenerator.item.repository.ItemRepository;
import com.sjw.doran.requestdatagenerator.item.service.ItemRequestGenerateService;
import com.sjw.doran.requestdatagenerator.member.repository.MemberRepository;
import com.sjw.doran.requestdatagenerator.member.service.MemberRequestGenerateService;
import com.sjw.doran.requestdatagenerator.order.repository.OrderRepository;
import com.sjw.doran.requestdatagenerator.order.service.OrderRequestGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/request-generate")
public class RequestGenerateController {

    private final MemberRequestGenerateService memberRequestGenerateService;
    private final OrderRequestGenerateService orderRequestGenerateService;
    private final ItemRequestGenerateService itemRequestGenerateService;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final Generator generator;
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    @PostMapping("/member/create")
    public String generateMemberRequests(@RequestParam("size") Long size) throws URISyntaxException, IOException, InterruptedException {

        Map<Long, MemberRequestGenerateService.MemberCreateRequest> requestMap = memberRequestGenerateService.createMemberCreateRequests(size);
        HttpClient client = HttpClient.newHttpClient();

        for (long i = 1; i <= size; i++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8000/auth-simple-service/users"))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            objectMapper.writeValueAsString(requestMap.get(i))))
                    .setHeader("Content-Type", "application/json")
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            if (i % 500 == 0)
                System.out.println("RequestGenerateController.generateMemberRequests" +
                        " - processed " + i + " requests.");
        }
        return "Members created";
    }

    @PostMapping("/basketitem/create")
    public String generateBasketItemRequests(@RequestParam("size") Long size) throws URISyntaxException, IOException, InterruptedException {

        Map<Long, MemberRequestGenerateService.BasketItemAddRequest> requestMap = memberRequestGenerateService.createBasketItemAddRequests(size);
        HttpClient client = HttpClient.newHttpClient();

        for (long i = 1; i <= size; i++) {
            String userUuid = memberRepository.findAnyUserUuid();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8000/member-service/basket/addItem"))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            objectMapper.writeValueAsString(requestMap.get(i))))
                    .setHeader("Content-Type", "application/json")
                    .setHeader("userUuid", userUuid)
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            if (i % 50 == 0)
                System.out.println("RequestGenerateController.generateBasketItemRequests" +
                        " - processed " + i + " requests.");
        }
        return "Basket Items created";
    }

    @PostMapping("/order/create")
    public String generateOrderRequests(@RequestParam("size") Long size) throws URISyntaxException, IOException, InterruptedException {

        Map<Long, OrderRequestGenerateService.OrderCreateRequest> requestMap = orderRequestGenerateService.createOrderCreateRequest(size);
        HttpClient client = HttpClient.newHttpClient();

        for (long i = 1; i <= size; i++) {
            String userUuid = memberRepository.findAnyUserUuid();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8000/order-service/orders"))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            objectMapper.writeValueAsString(requestMap.get(i))))
                    .setHeader("Content-Type", "application/json")
                    .setHeader("userUuid", userUuid)
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            if (i % 500 == 0)
                System.out.println("RequestGenerateController.generateOrderRequests" +
                        " - processed " + i + " requests.");
        }
        return "Orders created";
    }

    @PostMapping("/item/create")
    public String generateItemRequests(@RequestParam("size") Long size) throws URISyntaxException, IOException, InterruptedException {

        Map<Long, ItemRequestGenerateService.BookCreateRequest> requestMap = itemRequestGenerateService.createBookCreateRequests(size);
        HttpClient client = HttpClient.newHttpClient();

        for (long i = 1; i <= size; i++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8000/item-service/book/register"))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            objectMapper.writeValueAsString(requestMap.get(i))))
                    .setHeader("Content-Type", "application/json")
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            if (i % 500 == 0)
                System.out.println("RequestGenerateController.generateItemRequests" +
                        " - processed " + i + " requests.");
        }
        return "Items created";
    }

    @GetMapping("/item/booklist")
    public List<Item> getBookList(@RequestParam("itemUuidList") List<String> itemUuidList) {

        return itemRepository.findByItemUuidIn(itemUuidList);
    }

    @GetMapping("/member/userUuids")
    public String generateUserUuidsFile() {
        memberRequestGenerateService.createHeaderValueJsonFile();
        return "File created";
    }

    @GetMapping("/item/itemUuids")
    public String generateItemUuidsFile() {
        itemRequestGenerateService.createBodyValueJsonFile();
        return "File created";
    }
}
