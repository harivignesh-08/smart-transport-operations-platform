package com.quiz.analytics_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseResponse(
    Long id,
    Long vehicleId,
    Long driverId,
    Long tripId,
    String category,
    BigDecimal amount,
    String description,
    String receiptUrl,
    LocalDateTime expenseDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
