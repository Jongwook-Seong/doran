package com.sjw.doran.authsimpleservice.controller;

import com.sjw.doran.authsimpleservice.client.MemberServiceClient;
import com.sjw.doran.authsimpleservice.dto.UserDto;
import com.sjw.doran.authsimpleservice.service.UserService;
import com.sjw.doran.authsimpleservice.vo.MemberResponse;
import com.sjw.doran.authsimpleservice.vo.UserJoinRequest;
import com.sjw.doran.authsimpleservice.vo.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthSimpleController {

    private final UserService userService;
    private final MemberServiceClient memberServiceClient;

    @PostMapping("/users")
    public ResponseEntity<MemberResponse> signUp(@RequestBody UserJoinRequest request) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(request, UserDto.class);

        UserDto createdUserDto = userService.createUser(userDto);
        MemberResponse memberResponse = memberServiceClient.joinMember(createdUserDto.getUserUuid());
        return ResponseEntity.status(HttpStatus.CREATED).body(memberResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserDto> userDtoList = userService.getUsers();
        List<UserResponse> userResponseList = new ArrayList<>();

        ModelMapper mapper = new ModelMapper();
        userDtoList.forEach(userDto -> userResponseList.add(mapper.map(userDto, UserResponse.class)));
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
