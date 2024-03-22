package com.sjw.doran.authservice.controller;

import com.sjw.doran.authservice.service.GoogleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GoogleController {

    private final GoogleService googleService;
}
