package com.quiz.trip_service.dto;

import jakarta.validation.constraints.NotBlank;

public record TripRequest(
    @NotBlank(message = "Origin is required")
    String origin,

    @NotBlank(message = "Destination is required")
    String destination,

    String vehicleType
) {}
