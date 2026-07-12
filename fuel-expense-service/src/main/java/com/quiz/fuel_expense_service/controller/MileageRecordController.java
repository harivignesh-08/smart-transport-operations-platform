package com.quiz.fuel_expense_service.controller;

import com.quiz.fuel_expense_service.dto.MileageRequest;
import com.quiz.fuel_expense_service.dto.MileageResponse;
import com.quiz.fuel_expense_service.service.MileageRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mileage")
public class MileageRecordController {

    private final MileageRecordService mileageRecordService;

    public MileageRecordController(MileageRecordService mileageRecordService) {
        this.mileageRecordService = mileageRecordService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<MileageResponse> createMileageRecord(@Valid @RequestBody MileageRequest request) {
        MileageResponse response = mileageRecordService.createMileageRecord(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<MileageResponse> getMileageRecordById(@PathVariable Long id) {
        MileageResponse response = mileageRecordService.getMileageRecordById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vehicle/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<MileageResponse>> getMileageRecordsByVehicleId(@PathVariable Long vehicleId) {
        List<MileageResponse> response = mileageRecordService.getMileageRecordsByVehicleId(vehicleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trip/{tripId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<MileageResponse>> getMileageRecordsByTripId(@PathVariable Long tripId) {
        List<MileageResponse> response = mileageRecordService.getMileageRecordsByTripId(tripId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<MileageResponse>> getAllMileageRecords() {
        List<MileageResponse> response = mileageRecordService.getAllMileageRecords();
        return ResponseEntity.ok(response);
    }
}
