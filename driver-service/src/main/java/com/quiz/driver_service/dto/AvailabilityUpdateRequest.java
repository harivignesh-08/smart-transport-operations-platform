package com.quiz.driver_service.dto;

import jakarta.validation.constraints.NotBlank;

public record AvailabilityUpdateRequest(
    @NotBlank(message = "Availability status is required")
    String availabilityStatus,

    Long vehicleId
) {}
