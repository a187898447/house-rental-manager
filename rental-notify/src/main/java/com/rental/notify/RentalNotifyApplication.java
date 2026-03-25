package com.rental.notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RentalNotifyApplication {
    public static void main(String[] args) {
        SpringApplication.run(RentalNotifyApplication.class, args);
    }
}
