package com.sjw.doran.authservice.controller;

import com.sjw.doran.authservice.dto.UserDto;
import com.sjw.doran.authservice.service.UserService;
import com.sjw.doran.authservice.util.ModelMapperUtil;
import com.sjw.doran.authservice.vo.request.UserJoinRequest;
import com.sjw.doran.authservice.vo.request.UserLoginRequest;
import lombok.RequiredArgsConstructor;
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
    private final ModelMapperUtil modelMapperUtil;

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody UserJoinRequest request) {
        UserDto userDto = modelMapperUtil.convertToUserDto(request);
        userService.join(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserLoginRequest request) {
//        UserDto userDto = modelMapperUtil.convertToUserDto(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
