package com.rental.bill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 账单服务启动类
 *
 * @author rental-team
 * @date 2026-04-01
 */
@SpringBootApplication
@MapperScan("com.rental.bill.mapper")
public class RentalBillApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalBillApplication.class, args);
    }
}
