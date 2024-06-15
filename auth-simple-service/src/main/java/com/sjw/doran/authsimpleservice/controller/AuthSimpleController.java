package com.sjw.doran.authsimpleservice.controller;

import com.sjw.doran.authsimpleservice.client.MemberServiceClient;
import com.sjw.doran.authsimpleservice.dto.UserDto;
import com.sjw.doran.authsimpleservice.mapper.UserMapper;
import com.sjw.doran.authsimpleservice.service.UserService;
import com.sjw.doran.authsimpleservice.vo.MemberResponse;
import com.sjw.doran.authsimpleservice.vo.UserJoinRequest;
import com.sjw.doran.authsimpleservice.vo.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthSimpleController {

    private final UserService userService;
    private final MemberServiceClient memberServiceClient;
    private final UserMapper userMapper;

    @PostMapping("/users")
    public ResponseEntity<MemberResponse> signUp(@RequestBody UserJoinRequest request) {

        UserDto userDto = userMapper.toUserDto(request);

        UserDto createdUserDto = userService.createUser(userDto);
        MemberResponse memberResponse = memberServiceClient.joinMember(createdUserDto.getUserUuid(), createdUserDto.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(memberResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserDto> userDtoList = userService.getUsers();
        List<UserResponse> userResponseList = new ArrayList<>();
        userDtoList.forEach(userDto -> userResponseList.add(userMapper.toUserResponse(userDto)));
        return ResponseEntity.status(HttpStatus.OK).body(userResponseList);
    }

    @DeleteMapping("/users")
    public ResponseEntity<MemberResponse> deleteUser(Authentication authentication,
                                                     @RequestHeader("userUuid") String userUuid) {
        userService.deleteUser(authentication.getName(), userUuid);
        MemberResponse memberResponse = memberServiceClient.deleteMember(userUuid);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(memberResponse);
    }
}
