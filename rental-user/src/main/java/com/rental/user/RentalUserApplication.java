package com.rental.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 用户服务启动类
 *
 * @author rental-team
 * @date 2026-04-01
 */
@SpringBootApplication
@MapperScan("com.rental.user.mapper")
public class RentalUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalUserApplication.class, args);
    }
}
