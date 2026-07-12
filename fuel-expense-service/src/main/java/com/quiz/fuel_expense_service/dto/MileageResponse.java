package com.quiz.fuel_expense_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MileageResponse(
    Long id,
    Long vehicleId,
    Long driverId,
    Long tripId,
    BigDecimal startOdometer,
    BigDecimal endOdometer,
    BigDecimal distanceKm,
    LocalDateTime recordedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
