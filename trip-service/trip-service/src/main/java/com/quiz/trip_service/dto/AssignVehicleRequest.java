package com.quiz.trip_service.dto;

import jakarta.validation.constraints.NotNull;

public record AssignVehicleRequest(
    @NotNull(message = "Vehicle ID is required")
    Long vehicleId
) {}
