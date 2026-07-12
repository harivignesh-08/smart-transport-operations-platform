package com.quiz.tracking_service.dto;

import java.time.LocalDateTime;

public record LivePositionResponse(
    Long tripId,
    Long vehicleId,
    Long driverId,
    Double latitude,
    Double longitude,
    Double speed,
    Double heading,
    String tripStatus,
    String vehicleLabel,
    LocalDateTime lastUpdated
) {}
