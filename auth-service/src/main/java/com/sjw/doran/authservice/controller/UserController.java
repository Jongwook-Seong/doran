package com.sjw.doran.authservice.controller;

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

    @PostMapping("/join")
    public String join(@RequestBody UserJoinRequest request) {
        System.out.println(request);
        return "joined.";
    }
}
