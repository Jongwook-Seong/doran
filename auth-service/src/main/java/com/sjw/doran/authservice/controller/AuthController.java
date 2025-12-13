package com.sjw.doran.authservice.controller;

import com.sjw.doran.authservice.dto.TokenDto;
import com.sjw.doran.authservice.service.AuthService;
import com.sjw.doran.authservice.vo.*;
import com.sjw.doran.authservice.dto.UserDto;
import com.sjw.doran.authservice.mapper.UserMapper;
import com.sjw.doran.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/join")
    public ResponseEntity<Void> signUp(@RequestBody UserJoinRequest request) {
        UserDto userDto = userMapper.toUserDto(request);
        userService.join(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        TokenDto tokenDto = authService.authorize(request.getUserId(), request.getPassword());
        UserDto userDto = userService.getUserByUserId(request.getUserId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenDto.getAccessToken());
        headers.set("refreshToken", tokenDto.getRefreshToken());
        headers.set("userUuid", userDto.getUserUuid());

        UserLoginResponse response = UserLoginResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .userUuid(userDto.getUserUuid())
                .build();

        return ResponseEntity.ok().headers(headers).body(response);
    }

    @GetMapping("/users/{userUuid}")
    public ResponseEntity<UserResponse> getUserByUserUuid(@PathVariable("userUuid") String userUuid) {
        UserDto userDto = userService.getUserByUserUuid(userUuid);
        UserResponse userResponse = userMapper.toUserResponse(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserDto> userDtoList = userService.getUsers();
        List<UserResponse> userResponseList = new ArrayList<>();
        userDtoList.forEach(userDto -> userResponseList.add(userMapper.toUserResponse(userDto)));
        return ResponseEntity.status(HttpStatus.OK).body(userResponseList);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestHeader("Authorization") String accessToken,
                                            @RequestHeader("refreshToken") String refreshToken) {
        TokenDto tokenDto = authService.reissue(accessToken, refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("accessToken", tokenDto.getAccessToken());
        headers.add("refreshToken", tokenDto.getRefreshToken());
        return ResponseEntity.ok(tokenDto);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken,
                                       @RequestHeader("refreshToken") String refreshToken) {
        authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users")
    public ResponseEntity<Void> modifyUserInfo(Authentication authentication,
                                           @RequestHeader("userUuid") String userUuid,
                                           @RequestBody UserModifyRequest request) {
        userService.modifyUserInfo(authentication.getName(), userUuid, request.getPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<UserValidationResponse> validationCheck(Authentication authentication,
                                                                  @RequestHeader("userUuid") String userUuid) {
        boolean isActivated = authService.validationCheck(authentication.getName(), userUuid);
        return ResponseEntity.ok(new UserValidationResponse(isActivated));
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(Authentication authentication,
                                                     @RequestHeader("userUuid") String userUuid,
                                                     @RequestHeader("Authorization") String accessToken,
                                                     @RequestHeader("refreshToken") String refreshToken) {
        userService.deleteUser(authentication.getName(), userUuid);
        authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok().build();
    }
}