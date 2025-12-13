package com.sjw.doran.authservice.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;

    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * Request로 들어온 JWT의 유효성을 검증하고 SecurityContext에 저장
     * @param request  The request to process
     * @param response The response associated with the request
     * @param filterChain    Provides access to the next filter in the chain for this filter to pass the request and response
     *                     to for further processing
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        log.info("JwtAuthenticationFilter.doFilter");

        // 로그인 요청은 JWT 검증 없이 통과
        if (requestURI.startsWith("/api/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 유효성 검증
        try {
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("인증 정보 '{}'를 SecurityContext에 저장했습니다. uri: {}", authentication.getName(), requestURI);
            }
        } catch (Exception e) {
            log.debug("JWT 토큰이 유효하지 않습니다. uri: {}, message: {}", requestURI, e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Request Header에서 토큰 정보를 꺼내오기
     * @param request
     * @return
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
