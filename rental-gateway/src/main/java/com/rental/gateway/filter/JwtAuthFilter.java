package com.rental.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret:house-rental-manager-jwt-secret-key-2026}")
    private String jwtSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        
        // 登录接口放行
        if (path.contains("/api/user/login") || 
            path.contains("/api/user/send-code") || 
            path.contains("/api/user/phone-login")) {
            return chain.filter(exchange);
        }

        String token = getTokenFromRequest(exchange.getRequest());
                
        // demo-token 直接放行
        if ("demo-token".equals(token)) {
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", "1")
                    .header("X-User-Role", "landlord")
                    .build();
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        }
                
        if (token == null || token.isEmpty()) {
            // 没有 token，返回 401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        
        try {
            // 验证 JWT - 检查是否是有效的 JWT 格式（必须包含 2 个点）
            if (!token.contains(".") || token.split("\\.").length != 3) {
                log.error("无效的 JWT 格式：token='{}', 长度={}", token, token.length());
                throw new IllegalArgumentException("Invalid JWT format: token must contain exactly 2 periods");
            }
                    
            log.debug("开始验证 JWT token: {}...", token.substring(0, Math.min(20, token.length())));
                    
            // 使用与 JwtUtils 相同的方式创建 SecretKey
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
                    
            String userId = claims.getSubject();
            String role = claims.get("role", String.class);
                    
            log.info("JWT 验证成功：userId={}, role={}", userId, role);
            
            // 将用户信息传递给下游服务
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-Role", role != null ? role : "landlord")
                    .build();
            
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
            
        } catch (IllegalArgumentException e) {
            // JWT 格式无效
            log.error("JWT 格式无效：{} - token='{}'", e.getMessage(), token);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        } catch (Exception e) {
            // JWT 无效，返回 401
            log.error("JWT 验证失败：{} - token='{}'", e.getMessage(), token);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private String getTokenFromRequest(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
