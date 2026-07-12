package com.quiz.fuel_expense_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FuelLogRequest(
    @NotNull(message = "Vehicle ID is required") Long vehicleId,
    Long driverId,
    Long tripId,
    @NotNull(message = "Fuel type is required") String fuelType,
    @NotNull @Positive BigDecimal quantityLiters,
    @NotNull @Positive BigDecimal costPerLiter,
    @NotNull @Positive BigDecimal odometerReading,
    String stationName,
    @NotNull(message = "Filled at timestamp is required") LocalDateTime filledAt
) {}
