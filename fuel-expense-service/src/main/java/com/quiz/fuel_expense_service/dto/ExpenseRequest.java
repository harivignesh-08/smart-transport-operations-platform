package com.quiz.fuel_expense_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseRequest(
    @NotNull(message = "Vehicle ID is required") Long vehicleId,
    Long driverId,
    Long tripId,
    @NotNull(message = "Category is required") String category,
    @NotNull @Positive BigDecimal amount,
    @NotBlank(message = "Description is required") String description,
    String receiptUrl,
    @NotNull(message = "Expense date is required") LocalDateTime expenseDate
) {}
