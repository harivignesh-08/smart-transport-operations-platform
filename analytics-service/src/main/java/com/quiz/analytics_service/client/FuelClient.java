package com.quiz.analytics_service.client;

import com.quiz.analytics_service.dto.ExpenseResponse;
import com.quiz.analytics_service.dto.FuelLogResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "fuel-expense-service")
public interface FuelClient {

    @GetMapping("/api/fuel-logs")
    List<FuelLogResponse> getAllFuelLogs(@RequestHeader("Authorization") String token);

    @GetMapping("/api/expenses")
    List<ExpenseResponse> getAllExpenses(@RequestHeader("Authorization") String token);
}
