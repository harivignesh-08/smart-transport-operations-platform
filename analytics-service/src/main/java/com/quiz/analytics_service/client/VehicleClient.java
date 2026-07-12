package com.quiz.analytics_service.client;

import com.quiz.analytics_service.dto.VehicleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "vehicle-service")
public interface VehicleClient {

    @GetMapping("/api/vehicles")
    List<VehicleResponse> getAllVehicles(@RequestHeader("Authorization") String token);

    @GetMapping("/api/vehicles/{id}")
    VehicleResponse getVehicleById(@RequestHeader("Authorization") String token, @PathVariable("id") Long id);

    @GetMapping("/api/vehicles/available")
    List<VehicleResponse> getAvailableVehicles(@RequestHeader("Authorization") String token);
}
