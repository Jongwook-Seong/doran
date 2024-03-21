package com.sjw.doran.authservice.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sjw.doran.authservice.dto.TokenDto;
import com.sjw.doran.authservice.entity.User;
import com.sjw.doran.authservice.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private final UserRepository userRepository;

    public TokenProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return true;
    }

    public DecodedJWT getDecodedJWT(String token) {
        return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build().verify(token);
    }
}
