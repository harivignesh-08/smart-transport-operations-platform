package com.quiz.fuel_expense_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class FuelExpenseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuelExpenseServiceApplication.class, args);
    }
}
