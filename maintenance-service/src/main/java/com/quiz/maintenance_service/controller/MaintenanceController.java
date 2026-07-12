package com.quiz.maintenance_service.controller;

import com.quiz.maintenance_service.dto.*;
import com.quiz.maintenance_service.entity.MaintenanceSchedule;
import com.quiz.maintenance_service.entity.PredictiveMaintenance;
import com.quiz.maintenance_service.entity.Repair;
import com.quiz.maintenance_service.service.MaintenanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    // --- SCHEDULES ---

    @PostMapping("/schedules")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MaintenanceSchedule> createSchedule(@Valid @RequestBody MaintenanceScheduleRequest request) {
        MaintenanceSchedule schedule = maintenanceService.createSchedule(request);
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<MaintenanceSchedule>> getAllSchedules() {
        List<MaintenanceSchedule> schedules = maintenanceService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/schedules/{id}")
    public ResponseEntity<MaintenanceSchedule> getScheduleById(@PathVariable Long id) {
        MaintenanceSchedule schedule = maintenanceService.getScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/schedules/vehicle/{vehicleId}")
    public ResponseEntity<List<MaintenanceSchedule>> getSchedulesByVehicleId(@PathVariable Long vehicleId) {
        List<MaintenanceSchedule> schedules = maintenanceService.getSchedulesByVehicleId(vehicleId);
        return ResponseEntity.ok(schedules);
    }

    @PutMapping("/schedules/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MaintenanceSchedule> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceScheduleRequest request) {
        MaintenanceSchedule schedule = maintenanceService.updateSchedule(id, request);
        return ResponseEntity.ok(schedule);
    }

    @PutMapping("/schedules/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MaintenanceSchedule> updateScheduleStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        MaintenanceSchedule schedule = maintenanceService.updateScheduleStatus(id, request.status());
        return ResponseEntity.ok(schedule);
    }

    @DeleteMapping("/schedules/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        maintenanceService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    // --- REPAIRS ---

    @PostMapping("/repairs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Repair> logRepair(@Valid @RequestBody RepairRequest request) {
        Repair repair = maintenanceService.logRepair(request);
        return new ResponseEntity<>(repair, HttpStatus.CREATED);
    }

    @GetMapping("/repairs")
    public ResponseEntity<List<Repair>> getAllRepairs() {
        List<Repair> repairs = maintenanceService.getAllRepairs();
        return ResponseEntity.ok(repairs);
    }

    @GetMapping("/repairs/{id}")
    public ResponseEntity<Repair> getRepairById(@PathVariable Long id) {
        Repair repair = maintenanceService.getRepairById(id);
        return ResponseEntity.ok(repair);
    }

    @GetMapping("/repairs/vehicle/{vehicleId}")
    public ResponseEntity<List<Repair>> getRepairsByVehicleId(@PathVariable Long vehicleId) {
        List<Repair> repairs = maintenanceService.getRepairsByVehicleId(vehicleId);
        return ResponseEntity.ok(repairs);
    }

    @PutMapping("/repairs/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Repair> updateRepair(
            @PathVariable Long id,
            @Valid @RequestBody RepairRequest request) {
        Repair repair = maintenanceService.updateRepair(id, request);
        return ResponseEntity.ok(repair);
    }

    @DeleteMapping("/repairs/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRepair(@PathVariable Long id) {
        maintenanceService.deleteRepair(id);
        return ResponseEntity.noContent().build();
    }

    // --- PREDICTIVE MAINTENANCE ---

    @PostMapping("/predict/{vehicleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PredictiveMaintenance> performPredictiveMaintenance(
            @PathVariable Long vehicleId,
            @Valid @RequestBody PredictiveMaintenanceRequest request) {
        PredictiveMaintenance prediction = maintenanceService.performPredictiveMaintenance(vehicleId, request);
        return new ResponseEntity<>(prediction, HttpStatus.CREATED);
    }

    @GetMapping("/predictions/vehicle/{vehicleId}")
    public ResponseEntity<List<PredictiveMaintenance>> getPredictionsByVehicleId(@PathVariable Long vehicleId) {
        List<PredictiveMaintenance> predictions = maintenanceService.getPredictionsByVehicleId(vehicleId);
        return ResponseEntity.ok(predictions);
    }
}
