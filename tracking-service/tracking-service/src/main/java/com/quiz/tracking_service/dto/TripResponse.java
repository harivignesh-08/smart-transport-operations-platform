package com.quiz.tracking_service.dto;

import java.time.LocalDateTime;

public record TripResponse(
    Long id,
    Long vehicleId,
    Long driverId,
    String status,
    String origin,
    String destination,
    LocalDateTime startedAt,
    LocalDateTime completedAt
) {}
