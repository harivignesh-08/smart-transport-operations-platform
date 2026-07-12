package com.quiz.fuel_expense_service.dto;

import com.quiz.fuel_expense_service.entity.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseResponse(
    Long id,
    Long vehicleId,
    Long driverId,
    Long tripId,
    ExpenseCategory category,
    BigDecimal amount,
    String description,
    String receiptUrl,
    LocalDateTime expenseDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
