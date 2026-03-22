package com.rental.bill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@MapperScan("com.rental.bill.mapper")
@SpringBootApplication
@EnableScheduling
public class RentalBillApplication {
    public static void main(String[] args) {
        SpringApplication.run(RentalBillApplication.class, args);
    }
}
