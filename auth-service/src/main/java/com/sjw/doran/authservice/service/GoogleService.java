package com.sjw.doran.authservice.service;

import com.sjw.doran.authservice.dto.OAuthInfo;
import com.sjw.doran.authservice.repository.UserRepository;
import com.sjw.doran.authservice.vo.response.GoogleInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GoogleService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    private final String TOKEN_REQUEST_URL = "";

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthService authService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public GoogleInfoResponse getGoogleInfo() {
        return GoogleInfoResponse.builder()
                .clientId(clientId)
                .redirectUri(redirectUri)
                .build();
    }

    public OAuthInfo googleLogin(String code) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(TOKEN_REQUEST_URL,
                HttpMethod.POST,
                getAccessToken(code),
                OAuthInfo.class).getBody();
    }

    private HttpEntity<MultiValueMap<String, String>> getAccessToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        return new HttpEntity<>(params, headers);
    }
}
