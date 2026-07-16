package com.example.orn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.orn.security.JwtAccessDeniedHandler;
import com.example.orn.security.JwtAuthenticationEntryPoint;
import com.example.orn.security.JwtAuthenticationFilter;
import com.example.orn.service.CustomUserDetailsService;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // enables @PreAuthorize / @PostAuthorize for Phase 2 role checks
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // Public endpoints that do not require a JWT.
    // NOTE: these are the same URLs that already existed before this change -
    // only their access rule changes (from "permitAll on /**" to an explicit
    // allow-list); no URLs were renamed or added.
    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/**",
            "/signup",
            "/",
            "/login",
            "/index",
            "/excel-upload",
            "/orn-entry",
            "/orn-matching",
            "/reports",
            "/expensetracker",
            "/css/**",
            "/js/**",
            "/frontend/**",
            "/favicon.ico"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 401
                    .accessDeniedHandler(jwtAccessDeniedHandler)           // 403
            )
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                    .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

   @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);


        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }
    
}
