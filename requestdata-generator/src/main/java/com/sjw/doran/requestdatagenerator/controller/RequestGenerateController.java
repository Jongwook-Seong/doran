package com.sjw.doran.requestdatagenerator.controller;

import com.sjw.doran.requestdatagenerator.common.CustomObjectMapper;
import com.sjw.doran.requestdatagenerator.common.Memory;
import com.sjw.doran.requestdatagenerator.item.entity.Item;
import com.sjw.doran.requestdatagenerator.item.repository.ItemRepository;
import com.sjw.doran.requestdatagenerator.item.service.ItemRequestGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/request-generate")
public class RequestGenerateController {

    private final ItemRequestGenerateService itemRequestGenerateService;
    private final ItemRepository itemRepository;
    private final Memory memory = Memory.getInstance();
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();

    @PostMapping("/item/create")
    public String generateItemRequests() throws URISyntaxException, IOException, InterruptedException {

        long size = 10;
        itemRequestGenerateService.setBookItems(size);
        HttpClient client = HttpClient.newHttpClient();

        for (long i = 1; i <= size; i++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8000/item-service/book/register"))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            objectMapper.writeValueAsString(memory.getItemDataMap().get(i))))
                    .setHeader("Content-Type", "application/json")
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        return "Items created";
    }

    @GetMapping("/item/booklist")
    public List<Item> getBookList(@RequestParam("itemUuidList") List<String> itemUuidList) {

        return itemRepository.findByItemUuidIn(itemUuidList);
    }
}
