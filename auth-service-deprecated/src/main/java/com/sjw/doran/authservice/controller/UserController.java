package com.sjw.doran.authservice.controller;

import com.sjw.doran.authservice.dto.TokenDto;
import com.sjw.doran.authservice.dto.UserDto;
import com.sjw.doran.authservice.service.AuthService;
import com.sjw.doran.authservice.service.UserService;
import com.sjw.doran.authservice.util.ModelMapperUtil;
import com.sjw.doran.authservice.vo.request.UserJoinRequest;
import com.sjw.doran.authservice.vo.request.UserLoginRequest;
import com.sjw.doran.authservice.vo.request.UserModifyRequest;
import com.sjw.doran.authservice.vo.response.TokenAndUserUuidResponse;
import com.sjw.doran.authservice.vo.response.UserResponse;
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

    @GetMapping("/users/{userUuid}")
    public ResponseEntity<UserResponse> getUserByUserUuid(@PathVariable String userUuid) {
        UserDto userDto = userService.getUserByUserUuid(userUuid);
        UserResponse userResponse = modelMapperUtil.convertToUserResponse(userDto);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserDto> users = userService.getUsers();
        List<UserResponse> userResponseList = modelMapperUtil.mapToUserResponseList(users);
        return ResponseEntity.ok(userResponseList);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(
            @RequestHeader("Authorization") String accessToken,
            @RequestHeader("refreshToken") String refreshToken) {

        TokenDto tokenDto = authService.reissue(accessToken, refreshToken);
        HttpHeaders headers = new HttpHeaders();
        headers.add("accessToken", tokenDto.getAccessToken());
        headers.add("refreshToken", tokenDto.getRefreshToken());
        return ResponseEntity.ok(tokenDto);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String accessToken,
            @RequestHeader("refreshToken") String refreshToken) {

        authService.logout(accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/users")
    public ResponseEntity<Void> modifyUserInfo(Authentication authentication,
                                               @RequestHeader("userUuid") String userUuid,
                                               @RequestBody UserModifyRequest request) {

        userService.modifyUserInfo(
                authentication.getName(), userUuid, request.getEmail(), request.getPassword());

        return ResponseEntity.status(HttpStatus.OK).build();
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
