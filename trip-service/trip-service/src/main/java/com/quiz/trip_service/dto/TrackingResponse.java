package com.quiz.trip_service.dto;

import java.time.LocalDateTime;

public record TrackingResponse(
    Long id,
    Long tripId,
    Long vehicleId,
    Long driverId,
    String status,
    LocalDateTime startedAt,
    LocalDateTime endedAt
) {}
