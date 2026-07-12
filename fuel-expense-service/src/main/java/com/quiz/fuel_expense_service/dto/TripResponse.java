package com.quiz.fuel_expense_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TripResponse(
    Long id,
    Long vehicleId,
    Long driverId,
    String status,
    BigDecimal distanceKm,
    LocalDateTime startTime,
    LocalDateTime endTime,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
