package com.quiz.trip_service.dto;

import jakarta.validation.constraints.NotNull;

public record TrackingStartRequest(
    @NotNull Long tripId,
    @NotNull Long vehicleId,
    @NotNull Long driverId,
    String origin,
    String destination
) {}
