package com.quiz.vehicle_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleRequest(
    @NotBlank(message = "License plate is required")
    String licensePlate,

    @NotBlank(message = "Make is required")
    String make,

    @NotBlank(message = "Model is required")
    String model,

    @NotNull(message = "Year is required")
    Integer year,

    String status, // ACTIVE, MAINTENANCE, INACTIVE

    String availabilityStatus, // AVAILABLE, ASSIGNED, UNAVAILABLE

    Long driverId
) {}
