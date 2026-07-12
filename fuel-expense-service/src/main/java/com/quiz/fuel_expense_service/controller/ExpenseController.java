package com.quiz.fuel_expense_service.controller;

import com.quiz.fuel_expense_service.dto.ExpenseRequest;
import com.quiz.fuel_expense_service.dto.ExpenseResponse;
import com.quiz.fuel_expense_service.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse response = expenseService.createExpense(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id) {
        ExpenseResponse response = expenseService.getExpenseById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vehicle/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByVehicleId(@PathVariable Long vehicleId) {
        List<ExpenseResponse> response = expenseService.getExpensesByVehicleId(vehicleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trip/{tripId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByTripId(@PathVariable Long tripId) {
        List<ExpenseResponse> response = expenseService.getExpensesByTripId(tripId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vehicle/{vehicleId}/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByVehicleIdAndCategory(
            @PathVariable Long vehicleId,
            @PathVariable String category) {
        List<ExpenseResponse> response = expenseService.getExpensesByVehicleIdAndCategory(vehicleId, category);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        List<ExpenseResponse> response = expenseService.getAllExpenses();
        return ResponseEntity.ok(response);
    }
}
