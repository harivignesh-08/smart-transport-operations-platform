package com.quiz.trip_service.client;

import com.quiz.trip_service.dto.AvailabilityUpdateRequest;
import com.quiz.trip_service.dto.DriverResponse;
import com.quiz.trip_service.dto.PerformanceUpdateRequest;
import com.quiz.trip_service.dto.PerformanceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "driver-service")
public interface DriverClient {

    @GetMapping("/api/drivers/{id}")
    DriverResponse getDriverById(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long id);

    @PutMapping("/api/drivers/{id}/availability")
    DriverResponse updateDriverAvailability(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long id,
            @RequestBody AvailabilityUpdateRequest request);

    @PostMapping("/api/drivers/{driverId}/performance")
    PerformanceResponse recordTripOutcome(
            @RequestHeader("Authorization") String token,
            @PathVariable("driverId") Long driverId,
            @RequestBody PerformanceUpdateRequest request);
}
