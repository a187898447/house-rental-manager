package com.rental.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API 网关启动类
 *
 * @author rental-team
 * @date 2026-04-01
 */
@SpringBootApplication
@EnableDiscoveryClient
public class RentalGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalGatewayApplication.class, args);
    }
}
