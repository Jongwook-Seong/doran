package com.sjw.doran.authservice.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sjw.doran.authservice.dto.TokenDto;
import com.sjw.doran.authservice.entity.User;
import com.sjw.doran.authservice.exception.AuthServiceException;
import com.sjw.doran.authservice.exception.ErrorCode;
import com.sjw.doran.authservice.redis.RedisUtil;
import com.sjw.doran.authservice.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private final UserRepository userRepository;
    private final RedisUtil redisUtil;

    public TokenProvider(UserRepository userRepository, RedisUtil redisUtil) {
        this.userRepository = userRepository;
        this.redisUtil = redisUtil;
    }

    public TokenDto createToken(String identity, String authorities) {

        User user = userRepository.findByIdentity(identity).get();

        String accessToken = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .withClaim("id", user.getId())
                .withClaim("identity", user.getIdentity())
                .withClaim(AUTHORITIES_KEY, authorities)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        String refreshToken = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME))
                .withClaim("id", user.getId())
                .withClaim("identity", user.getIdentity())
                .withClaim(AUTHORITIES_KEY, authorities)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return new TokenDto(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {

        DecodedJWT decodedToken = getDecodedJWT(token);

        List<SimpleGrantedAuthority> authorities =
                Arrays.stream(
                        decodedToken.getClaim(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        decodedToken.getClaim("identity"), null, authorities);

        return authenticationToken;
    }

    public boolean validateToken(String token) {
        try {
            if (redisUtil.hasKeyBlackList(token))
                throw new AuthServiceException(ErrorCode.INVALID_ACCESS_TOKEN, "로그아웃 또는 탈퇴한 회원입니다.");
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
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

    public DecodedJWT getDecodedJWT(String token) {
        return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build().verify(token);
    }
}
