package com.sartora.Sartora_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class WebSecurityConfig {

    @Autowired
    private JWTRequestFilter jwtRequestFilter;

    @Bean

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())   // Disable CSRF protection
//                .cors(cors -> cors.disable())   // Disable CORS
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()   // Allow all requests without authentication
//                );
        http
                .cors(cors -> cors.disable()) // Disables CORS using lambda
                .csrf(csrf -> csrf.disable()) // Disables CSRF using lambda
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/product", "/api/v1/register","/api/v1/login", "/api/v1/verify",
                                "/api/v1/forgot","/api/v1/reset").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Adding your JWT filter

        return http.build();
    }
}
