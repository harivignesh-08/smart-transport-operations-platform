package com.quiz.fuel_expense_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MileageRequest(
    @NotNull(message = "Vehicle ID is required") Long vehicleId,
    Long driverId,
    Long tripId,
    @NotNull @Positive BigDecimal startOdometer,
    @NotNull @Positive BigDecimal endOdometer,
    @NotNull(message = "Recorded at timestamp is required") LocalDateTime recordedAt
) {}
