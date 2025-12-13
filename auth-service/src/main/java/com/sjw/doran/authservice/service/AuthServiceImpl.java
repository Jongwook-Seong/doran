package com.sjw.doran.authservice.service;

import com.sjw.doran.authservice.dto.TokenDto;
import com.sjw.doran.authservice.entity.UserEntity;
import com.sjw.doran.authservice.exception.AuthServiceException;
import com.sjw.doran.authservice.exception.ErrorCode;
import com.sjw.doran.authservice.jwt.TokenProvider;
import com.sjw.doran.authservice.redis.service.KeyRedisService;
import com.sjw.doran.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final KeyRedisService keyRedisService;
    private final UserRepository userRepository;

    @Override
    public TokenDto authorize(String userId, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String authorities = getAuthorities(authentication);
        return tokenProvider.createToken(authentication.getName(), authorities);
    }

    @Override
    public TokenDto reissue(String accessToken, String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new AuthServiceException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String authorities = getAuthorities(authentication);
        return tokenProvider.createToken(authentication.getName(), authorities);
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        keyRedisService.setBlackList(accessToken, "accessToken", 10080);
        keyRedisService.setBlackList(refreshToken, "refreshToken", 43200);
    }

    @Override
    public boolean validationCheck(String userId, String userUuid) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new AuthServiceException(ErrorCode.USER_NOT_FOUND));
        if (!userEntity.getUserUuid().equals(userUuid)) {
            throw new AuthServiceException(ErrorCode.INVALID_USER_UUID);
        }
        return userEntity.isActivated();
    }

    /**
     * Authentication 객체에서 권한 정보를 추출하여 문자열로 변환
     * @param authentication
     * @return
     */
    private String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
