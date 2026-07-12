package com.quiz.analytics_service.client;

import com.quiz.analytics_service.dto.MaintenanceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "maintenance-service")
public interface MaintenanceClient {

    @GetMapping("/api/maintenance")
    List<MaintenanceResponse> getAllMaintenance(@RequestHeader("Authorization") String token);

    @GetMapping("/api/maintenance/vehicle/{vehicleId}")
    List<MaintenanceResponse> getMaintenanceByVehicleId(@RequestHeader("Authorization") String token, @PathVariable("vehicleId") Long vehicleId);
}
