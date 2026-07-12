package com.quiz.fuel_expense_service.dto;

import com.quiz.fuel_expense_service.entity.FuelType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FuelLogResponse(
    Long id,
    Long vehicleId,
    Long driverId,
    Long tripId,
    FuelType fuelType,
    BigDecimal quantityLiters,
    BigDecimal costPerLiter,
    BigDecimal totalCost,
    BigDecimal odometerReading,
    String stationName,
    LocalDateTime filledAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
