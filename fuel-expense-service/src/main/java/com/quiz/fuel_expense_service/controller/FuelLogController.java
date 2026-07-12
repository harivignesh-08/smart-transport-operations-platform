package com.quiz.fuel_expense_service.controller;

import com.quiz.fuel_expense_service.dto.FuelLogRequest;
import com.quiz.fuel_expense_service.dto.FuelLogResponse;
import com.quiz.fuel_expense_service.service.FuelLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuel-logs")
public class FuelLogController {

    private final FuelLogService fuelLogService;

    public FuelLogController(FuelLogService fuelLogService) {
        this.fuelLogService = fuelLogService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<FuelLogResponse> createFuelLog(@Valid @RequestBody FuelLogRequest request) {
        FuelLogResponse response = fuelLogService.createFuelLog(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<FuelLogResponse> getFuelLogById(@PathVariable Long id) {
        FuelLogResponse response = fuelLogService.getFuelLogById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vehicle/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<FuelLogResponse>> getFuelLogsByVehicleId(@PathVariable Long vehicleId) {
        List<FuelLogResponse> response = fuelLogService.getFuelLogsByVehicleId(vehicleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trip/{tripId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<FuelLogResponse>> getFuelLogsByTripId(@PathVariable Long tripId) {
        List<FuelLogResponse> response = fuelLogService.getFuelLogsByTripId(tripId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<FuelLogResponse>> getAllFuelLogs() {
        List<FuelLogResponse> response = fuelLogService.getAllFuelLogs();
        return ResponseEntity.ok(response);
    }
}
