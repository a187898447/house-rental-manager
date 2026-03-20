package com.rental.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
@Slf4j
public class JwtUtils {

    // 密钥（生产环境应从配置读取）
    private static final String SECRET = "house-rental-manager-jwt-secret-key-2026";
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L; // 7天

    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * 生成 JWT Token
     */
    public static String generateToken(Long userId, String openid, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("openid", openid);
        claims.put("role", role);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRE_TIME);

        return Jwts.builder()
                .claims(claims)
                .subject(openid)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(KEY)
                .compact();
    }

    /**
     * 解析 Token
     */
    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("JWT Token 已过期: {}", e.getMessage());
            throw e;
        } catch (JwtException e) {
            log.warn("JWT Token 无效: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 验证 Token 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 获取 openid
     */
    public static String getOpenid(String token) {
        Claims claims = parseToken(token);
        return claims.get("openid", String.class);
    }

    /**
     * 获取角色
     */
    public static String getRole(String token) {
        Claims claims = parseToken(token);
        return claims.get("role", String.class);
    }

    /**
     * 判断 Token 是否过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}
