package com.sjw.doran.authservice.jwt;

import com.sjw.doran.authservice.dto.TokenDto;
import com.sjw.doran.authservice.entity.UserEntity;
import com.sjw.doran.authservice.exception.AuthServiceException;
import com.sjw.doran.authservice.exception.ErrorCode;
import com.sjw.doran.authservice.redis.service.KeyRedisService;
import com.sjw.doran.authservice.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private final KeyRedisService keyRedisService;
    private Key key;
    private final UserRepository userRepository;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-millis}") long accessTokenValidityInMilliseconds,
            @Value("${jwt.refresh-token-validity-in-millis}") long refreshTokenValidityInMilliseconds,
            KeyRedisService keyRedisService,
            UserRepository userRepository) {
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
        this.keyRedisService = keyRedisService;
        this.userRepository = userRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Authorization 객체의 권한 정보를 이용해서 토큰을 생성하는 메소드

    /**
     * Authorization 객체의 권한 정보를 이용해서 토큰을 생성하는 메소드
     * @param userId
     * @return
     */
    public TokenDto createToken(String userId, String authorities) {
        long now = (new java.util.Date()).getTime();
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new AuthServiceException(ErrorCode.USER_NOT_FOUND));

        String accessToken = Jwts.builder()
                .claim("userId", userEntity.getUserId())
                .claim("email", userEntity.getEmail())
                .claim(AUTHORITIES_KEY, authorities)
                .subject(userEntity.getUserUuid())
                .expiration(new Date(now + this.accessTokenValidityInMilliseconds))
                .signWith(key)
                .compact();

        String refreshToken = Jwts.builder()
                .claim("userId", userEntity.getUserId())
                .claim("email", userEntity.getEmail())
                .claim(AUTHORITIES_KEY, authorities)
                .subject(userEntity.getUserUuid())
                .expiration(new Date(now + this.refreshTokenValidityInMilliseconds))
                .signWith(key)
                .compact();

        return new TokenDto(accessToken, refreshToken);
    }

    /**
     * 토큰을 파라미터로 받아 토큰에 담겨있는 정보로 Authentication 객체를 리턴하는 메소드
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        return new UsernamePasswordAuthenticationToken(claims.get("userId"), null, authorities);
    }

    /**
     * 토큰의 유효성 검증 메소드
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token);
            if (keyRedisService.hasKeyBlackList(token)) {
                throw new AuthServiceException(ErrorCode.INVALID_ACCESS_TOKEN, "로그아웃 또는 탈퇴한 회원입니다.");
            }
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    /**
     * 토큰을 파라미터로 받아 토큰에 담겨있는 정보를 꺼내는 메소드
     * @param token
     * @return Claims
     */
    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
