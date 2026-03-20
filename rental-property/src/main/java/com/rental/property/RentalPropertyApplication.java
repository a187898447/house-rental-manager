package com.rental.property;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@MapperScan("com.rental.property.mapper")
@SpringBootApplication
public class RentalPropertyApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalPropertyApplication.class, args);
    }
}
