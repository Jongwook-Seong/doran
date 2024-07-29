package com.fastcampus.kafkahandson.requestdatagenerator.controller;

import com.fastcampus.kafkahandson.requestdatagenerator.common.CustomObjectMapper;
import com.fastcampus.kafkahandson.requestdatagenerator.common.Memory;
import com.fastcampus.kafkahandson.requestdatagenerator.service.ItemRequestGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/request-generate")
public class RequestGenerateController {

    private final ItemRequestGenerateService itemRequestGenerateService;
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
}
