package com.sjw.doran.authservice.service;

import com.sjw.doran.authservice.dto.TokenDto;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    @Transactional(readOnly = true)
    TokenDto authorize(String identity, String password);

    TokenDto reissue(String accessToken, String refreshToken);

    void logout(String accessToken, String refreshToken);

    boolean validateCheck(String identity, String userUuid);
}
