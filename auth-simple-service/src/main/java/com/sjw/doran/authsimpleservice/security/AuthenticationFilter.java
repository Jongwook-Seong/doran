package com.sjw.doran.authsimpleservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjw.doran.authsimpleservice.vo.UserLoginResponse;
import com.sjw.doran.authsimpleservice.dto.UserDto;
import com.sjw.doran.authsimpleservice.redis.service.RedisService;
import com.sjw.doran.authsimpleservice.service.UserService;
import com.sjw.doran.authsimpleservice.util.JwtUtil;
import com.sjw.doran.authsimpleservice.vo.UserLoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil, RedisService redisService) {
        super(authenticationManager);
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginRequest creds = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUserId(), // In this project, userId is used as the username (email)
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String username = ((User)authResult.getPrincipal()).getUsername();
        UserDto userDto = userService.getUserDetailsByUsername(username);

        String accessToken = jwtUtil.createAccessToken(userDto);
        String refreshToken = jwtUtil.createRefreshToken(userDto);

        redisService.saveRefreshToken(username, refreshToken);

        UserLoginResponse loginResponse = new UserLoginResponse(accessToken, refreshToken, userDto.getUserId());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(loginResponse));
        log.info("Login successful for user: {}", username);
    }
}