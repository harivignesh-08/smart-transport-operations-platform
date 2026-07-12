package com.quiz.tracking_service.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record GpsUpdateRequest(
    @NotNull(message = "Trip ID is required")
    Long tripId,

    @NotNull(message = "Vehicle ID is required")
    Long vehicleId,

    Long driverId,

    @NotNull(message = "Latitude is required")
    Double latitude,

    @NotNull(message = "Longitude is required")
    Double longitude,

    Double speed,

    Double heading,

    LocalDateTime recordedAt
) {}
