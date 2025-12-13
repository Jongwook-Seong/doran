package com.sjw.doran.authsimpleservice.controller;

import com.sjw.doran.authsimpleservice.client.MemberServiceClient;
import com.sjw.doran.authsimpleservice.vo.UserLoginResponse;
import com.sjw.doran.authsimpleservice.dto.UserDto;
import com.sjw.doran.authsimpleservice.mapper.UserMapper;
import com.sjw.doran.authsimpleservice.redis.service.RedisService;
import com.sjw.doran.authsimpleservice.service.UserService;
import com.sjw.doran.authsimpleservice.util.JwtUtil;
import com.sjw.doran.authsimpleservice.vo.MemberResponse;
import com.sjw.doran.authsimpleservice.vo.UserJoinRequest;
import com.sjw.doran.authsimpleservice.vo.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthSimpleController {

    private final UserService userService;
    private final MemberServiceClient memberServiceClient;
    private final UserMapper userMapper;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;

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

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal User user) {
        redisService.deleteRefreshToken(user.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<UserLoginResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Authorization-Refresh");
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtUtil.getUsername(refreshToken);
        String storedRefreshToken = redisService.getRefreshToken(username);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDto userDto = userService.getUserDetailsByUsername(username);

        String newAccessToken = jwtUtil.createAccessToken(userDto);
        String newRefreshToken = jwtUtil.createRefreshToken(userDto);

        redisService.saveRefreshToken(username, newRefreshToken);

        UserLoginResponse loginResponse = new UserLoginResponse(newAccessToken, newRefreshToken, userDto.getUserId());
        return ResponseEntity.ok(loginResponse);
    }
}