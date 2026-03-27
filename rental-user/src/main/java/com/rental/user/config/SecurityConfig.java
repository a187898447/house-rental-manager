package com.rental.user.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置
 * 注意：微信小程序使用 JWT，无需 Session
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
            // 禁用 CSRF（前后端分离）
            .csrf(AbstractHttpConfigurer::disable)
            // 禁用 Session（使用 JWT）
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 添加 JWT 过滤器
            .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
            // 配置请求授权
            .authorizeHttpRequests(auth -> auth
                // 登录接口无需认证
                .requestMatchers("/user/login", "/user/bind-phone", "/user/info", "/user", "/doc.html", "/swagger-ui/**", "/v3/api-docs/**", "/api/statistics/**", "/statistics/**").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated())
            // 禁用默认登录页
            .formLogin(form -> form.disable())
            // 禁用 HTTP Basic
            .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
