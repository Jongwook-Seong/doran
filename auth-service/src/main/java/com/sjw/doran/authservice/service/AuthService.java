package com.sjw.doran.authservice.service;

import com.sjw.doran.authservice.dto.TokenDto;

public interface AuthService {

    TokenDto authorize(String userId, String password);
    TokenDto reissue(String accessToken, String refreshToken);
    void logout(String accessToken, String refreshToken);
    boolean validationCheck(String userId, String userUuid);
}
