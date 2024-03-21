package com.sjw.doran.authservice.config;

import com.sjw.doran.authservice.jwt.JwtAuthenticationFilter;
import com.sjw.doran.authservice.jwt.JwtAuthorizationFilter;
import com.sjw.doran.authservice.repository.UserRepository;
import com.sjw.doran.authservice.service.PrincipalDetailsService;
import com.sjw.doran.authservice.service.PrincipalOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalDetailsService principalDetailsService;
    private final CorsConfig corsConfig;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);
        sharedObject.userDetailsService(principalDetailsService);
        AuthenticationManager authenticationManager = sharedObject.build();
        http.authenticationManager(authenticationManager);

        http.csrf(CsrfConfigurer::disable);

        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilter(corsConfig.corsFilter());
        http.addFilter(new JwtAuthenticationFilter(authenticationManager));
        http.addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/user/**").authenticated()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll());

        return http.build();
    }
}
