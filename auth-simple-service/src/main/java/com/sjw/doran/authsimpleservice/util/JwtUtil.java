package com.sjw.doran.authsimpleservice.util;

import com.sjw.doran.authsimpleservice.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;

    public JwtUtil(@Value("${token.secret}") String secretKey,
                   @Value("${token.access-expiration-time}") long accessTokenExpireTime,
                   @Value("${token.refresh-expiration-time}") long refreshTokenExpireTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public String createAccessToken(UserDto userDto) {
        return createToken(userDto, accessTokenExpireTime);
    }

    public String createRefreshToken(UserDto userDto) {
        return createToken(userDto, refreshTokenExpireTime);
    }

    private String createToken(UserDto userDto, long expireTime) {
        Date now = new Date();
        return Jwts.builder()
                .claim("username", userDto.getUsername())
                .claim("userId", userDto.getUserId())
                .claim("identity", userDto.getIdentity())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expireTime))
                .signWith(key)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token) {
        return parseClaims(token).get("username", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
