package com.quiz.maintenance_service.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PredictiveMaintenanceRequest(
    @NotNull(message = "Total distance is required")
    Double totalDistance,

    @NotNull(message = "Engine hours is required")
    Double engineHours,

    @NotNull(message = "Fuel consumption is required")
    Double fuelConsumption,

    @NotNull(message = "Last service date is required")
    LocalDate lastServiceDate
) {}
