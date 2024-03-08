package com.sjw.doran.memberservice.service.impl;

import com.sjw.doran.memberservice.repository.BasketRepository;
import com.sjw.doran.memberservice.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;
}
