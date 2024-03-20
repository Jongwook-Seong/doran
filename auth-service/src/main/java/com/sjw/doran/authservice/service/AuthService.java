package com.sjw.doran.authservice.service;

import com.sjw.doran.authservice.dto.TokenDto;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    @Transactional(readOnly = true)
    TokenDto authorize(String identity, String password);
}
