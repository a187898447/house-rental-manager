package com.rental.property;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 房源服务启动类
 *
 * @author rental-team
 * @date 2026-04-01
 */
@SpringBootApplication
@MapperScan("com.rental.property.mapper")
@EnableDiscoveryClient
public class RentalPropertyApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalPropertyApplication.class, args);
    }
}
