package com.rental.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * JWT 认证过滤器
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    /**
     * 免认证路径
     */
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/api/user/wx-login",
            "/api/user/phone-login"
    );

    /**
     * Token 前缀
     */
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        log.info("网关请求：method={}, path={}", request.getMethod(), path);

        // 1. 检查是否为免认证路径
        if (isExcludePath(path)) {
            log.debug("免认证路径：{}", path);
            return chain.filter(exchange);
        }

        // 2. 获取 Token
        String token = extractToken(request);
        if (!StringUtils.hasText(token)) {
            log.warn("缺少 Token: path={}", path);
            return unAuthorized(exchange, "未授权访问");
        }

        // 3. 验证 Token（简化实现，实际应调用用户服务验证）
        if (!validateToken(token)) {
            log.warn("Token 无效：path={}", path);
            return unAuthorized(exchange, "Token 无效或已过期");
        }

        // 4. 传递用户信息到下游服务
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-User-Id", getUserIdFromToken(token))
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    @Override
    public int getOrder() {
        return -100;
    }

    /**
     * 是否为免认证路径
     */
    private boolean isExcludePath(String path) {
        return EXCLUDE_PATHS.stream().anyMatch(path::startsWith);
    }

    /**
     * 提取 Token
     */
    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(TOKEN_PREFIX)) {
            return authHeader.substring(TOKEN_PREFIX.length());
        }
        return request.getQueryParams().getFirst("token");
    }

    /**
     * 验证 Token（简化实现）
     */
    private boolean validateToken(String token) {
        // TODO: 实际应调用用户服务验证或解析 JWT
        return StringUtils.hasText(token) && token.length() > 10;
    }

    /**
     * 从 Token 中获取用户 ID（简化实现）
     */
    private String getUserIdFromToken(String token) {
        // TODO: 实际应解析 JWT 获取用户 ID
        return "1000000001";
    }

    /**
     * 返回 401 未授权
     */
    private Mono<Void> unAuthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().set("Content-Type", "application/json;charset=UTF-8");
        String body = String.format("{\"code\":401,\"message\":\"%s\"}", message);
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(body.getBytes()))
        );
    }
}
