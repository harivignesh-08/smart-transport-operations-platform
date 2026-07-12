package com.quiz.analytics_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FuelLogResponse(
    Long id,
    Long vehicleId,
    Long driverId,
    Long tripId,
    String fuelType,
    BigDecimal quantityLiters,
    BigDecimal costPerLiter,
    BigDecimal totalCost,
    BigDecimal odometerReading,
    String stationName,
    LocalDateTime filledAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
