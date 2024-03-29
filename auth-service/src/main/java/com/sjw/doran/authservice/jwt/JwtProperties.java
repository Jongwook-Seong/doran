package com.sjw.doran.authservice.jwt;

public interface JwtProperties {

    String SECRET = "auth";
    int ACCESS_TOKEN_EXPIRATION_TIME = 60000 * 30;
    int REFRESH_TOKEN_EXPIRATION_TIME = 60000 * 60 * 24;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
