package com.quiz.maintenance_service.client;

import com.quiz.maintenance_service.dto.AvailabilityUpdateRequest;
import com.quiz.maintenance_service.dto.StatusUpdateRequest;
import com.quiz.maintenance_service.dto.VehicleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "vehicle-service")
public interface VehicleClient {

    @GetMapping("/api/vehicles/{id}")
    VehicleResponse getVehicleById(@PathVariable("id") Long id);

    @PutMapping("/api/vehicles/{id}/status")
    VehicleResponse updateVehicleStatus(@PathVariable("id") Long id, @RequestBody StatusUpdateRequest request);

    @PutMapping("/api/vehicles/{id}/availability")
    VehicleResponse updateVehicleAvailability(@PathVariable("id") Long id, @RequestBody AvailabilityUpdateRequest request);
}
