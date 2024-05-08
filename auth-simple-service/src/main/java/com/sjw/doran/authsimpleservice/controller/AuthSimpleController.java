package com.sjw.doran.authsimpleservice.controller;

import com.sjw.doran.authsimpleservice.vo.RequestUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthSimpleController {

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody RequestUser user) {
        return null;
    }
}
