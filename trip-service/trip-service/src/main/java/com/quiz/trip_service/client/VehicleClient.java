package com.quiz.trip_service.client;

import com.quiz.trip_service.dto.AvailabilityUpdateRequest;
import com.quiz.trip_service.dto.VehicleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "vehicle-service")
public interface VehicleClient {

    @GetMapping("/api/vehicles/{id}")
    VehicleResponse getVehicleById(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long id);

    @PutMapping("/api/vehicles/{id}/availability")
    VehicleResponse updateVehicleAvailability(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long id,
            @RequestBody AvailabilityUpdateRequest request);
}
