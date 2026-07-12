package com.quiz.tracking_service.dto;

import java.time.LocalDateTime;

public record GpsLocationResponse(
    Long id,
    Long tripId,
    Long vehicleId,
    Long driverId,
    Double latitude,
    Double longitude,
    Double speed,
    Double heading,
    LocalDateTime recordedAt
) {}
