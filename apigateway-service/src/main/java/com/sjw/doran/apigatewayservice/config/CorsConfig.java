package com.sjw.doran.apigatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

//@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // 허용할 출처 설정 (프론트엔드 주소)
        corsConfig.setAllowedOriginPatterns(Arrays.asList("*"));
//        corsConfig.setAllowedOriginPatterns(Collections.singletonList("http://localhost:3001"));

        // 허용할 HTTP 메서드 설정
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 허용할 헤더 설정
        corsConfig.setAllowedHeaders(Arrays.asList("*"));
        
        // 인증 정보 허용 (쿠키, Authorization 헤더 등)
        corsConfig.setAllowCredentials(true);
        
        // Preflight 요청의 캐시 시간 설정 (초 단위)
        corsConfig.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        
        return new CorsWebFilter(source);
    }
}


