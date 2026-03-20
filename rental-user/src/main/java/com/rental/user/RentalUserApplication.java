package com.rental.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@MapperScan("com.rental.user.mapper")
@SpringBootApplication
public class RentalUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalUserApplication.class, args);
    }
}
