package com.sjw.doran.authservice.controller;

import com.sjw.doran.authservice.dto.UserDto;
import com.sjw.doran.authservice.service.UserService;
import com.sjw.doran.authservice.util.ModelMapperUtil;
import com.sjw.doran.authservice.vo.request.UserJoinRequest;
import lombok.RequiredArgsConstructor;
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
    public String join(@RequestBody UserJoinRequest request) {
        System.out.println(request);
        UserDto userDto = modelMapperUtil.convertToUserDto(request);
        userService.join(userDto);
        return "joined.";
    }
}
