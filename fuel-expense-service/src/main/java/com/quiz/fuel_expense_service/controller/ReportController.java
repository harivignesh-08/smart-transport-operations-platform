package com.quiz.fuel_expense_service.controller;

import com.quiz.fuel_expense_service.dto.ExpenseSummaryReport;
import com.quiz.fuel_expense_service.dto.FuelSummaryReport;
import com.quiz.fuel_expense_service.dto.MileageSummaryReport;
import com.quiz.fuel_expense_service.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/fuel/vehicle/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<FuelSummaryReport> getFuelReport(
            @PathVariable Long vehicleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        FuelSummaryReport report = reportService.generateFuelReport(vehicleId, from, to);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/expenses/vehicle/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<ExpenseSummaryReport> getExpenseReport(
            @PathVariable Long vehicleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        ExpenseSummaryReport report = reportService.generateExpenseReport(vehicleId, from, to);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/mileage/vehicle/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<MileageSummaryReport> getMileageReport(
            @PathVariable Long vehicleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        MileageSummaryReport report = reportService.generateMileageReport(vehicleId, from, to);
        return ResponseEntity.ok(report);
    }
}
