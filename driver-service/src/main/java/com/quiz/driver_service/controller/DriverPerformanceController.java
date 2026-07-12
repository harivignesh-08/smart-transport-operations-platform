package com.quiz.driver_service.controller;

import com.quiz.driver_service.dto.PerformanceResponse;
import com.quiz.driver_service.dto.PerformanceUpdateRequest;
import com.quiz.driver_service.service.DriverPerformanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drivers")
public class DriverPerformanceController {

    private final DriverPerformanceService performanceService;

    public DriverPerformanceController(DriverPerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping("/{driverId}/performance")
    public ResponseEntity<PerformanceResponse> getPerformanceByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(performanceService.getPerformanceByDriver(driverId));
    }

    @PostMapping("/{driverId}/performance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PerformanceResponse> recordTripOutcome(
            @PathVariable Long driverId,
            @Valid @RequestBody PerformanceUpdateRequest request) {
        return ResponseEntity.ok(performanceService.recordTripOutcome(driverId, request));
    }
}
