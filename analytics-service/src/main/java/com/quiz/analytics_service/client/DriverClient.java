package com.quiz.analytics_service.client;

import com.quiz.analytics_service.dto.DriverResponse;
import com.quiz.analytics_service.dto.PerformanceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "driver-service")
public interface DriverClient {

    @GetMapping("/api/drivers")
    List<DriverResponse> getAllDrivers(@RequestHeader("Authorization") String token);

    @GetMapping("/api/drivers/{id}")
    DriverResponse getDriverById(@RequestHeader("Authorization") String token, @PathVariable("id") Long id);

    @GetMapping("/api/drivers/available")
    List<DriverResponse> getAvailableDrivers(@RequestHeader("Authorization") String token);

    @GetMapping("/api/drivers/{driverId}/performance")
    PerformanceResponse getPerformanceByDriver(@RequestHeader("Authorization") String token, @PathVariable("driverId") Long driverId);
}
