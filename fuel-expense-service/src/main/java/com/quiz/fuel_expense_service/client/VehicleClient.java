package com.quiz.fuel_expense_service.client;

import com.quiz.fuel_expense_service.dto.VehicleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "vehicle-service")
public interface VehicleClient {

    @GetMapping("/api/vehicles/{id}")
    VehicleResponse getVehicleById(@RequestHeader("Authorization") String token, @PathVariable("id") Long id);
}
