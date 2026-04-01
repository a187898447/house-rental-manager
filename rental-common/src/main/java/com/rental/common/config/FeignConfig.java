package com.rental.common.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * Feign 配置
 *
 * @author rental-team
 * @date 2026-04-01
 */
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
