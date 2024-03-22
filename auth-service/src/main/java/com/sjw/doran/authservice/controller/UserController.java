package com.sjw.doran.authservice.controller;

import com.sjw.doran.authservice.dto.TokenDto;
import com.sjw.doran.authservice.dto.UserDto;
import com.sjw.doran.authservice.service.AuthService;
import com.sjw.doran.authservice.service.UserService;
import com.sjw.doran.authservice.util.ModelMapperUtil;
import com.sjw.doran.authservice.vo.request.UserJoinRequest;
import com.sjw.doran.authservice.vo.request.UserLoginRequest;
import com.sjw.doran.authservice.vo.response.TokenAndUserUuidResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final ModelMapperUtil modelMapperUtil;

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody UserJoinRequest request) {
        UserDto userDto = modelMapperUtil.convertToUserDto(request);
        userService.join(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenAndUserUuidResponse> login(@RequestBody UserLoginRequest request) {
        TokenDto tokenDto = authService.authorize(request.getIdentity(), request.getPassword());
        UserDto userDto = userService.getUserByIdentity(request.getIdentity());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenDto.getAccessToken());
        headers.set("refreshToken", tokenDto.getRefreshToken());
        headers.set("userUuid", userDto.getUserUuid());

        TokenAndUserUuidResponse response = TokenAndUserUuidResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .userUuid(userDto.getUserUuid())
                .build();

        return ResponseEntity.ok().headers(headers).body(response);
    }
}
