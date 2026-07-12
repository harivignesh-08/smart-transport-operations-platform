package com.quiz.notification_service.dto;

import java.time.LocalDateTime;

public record TripResponse(
    Long id,
    Long driverId,
    Long vehicleId,
    String pickupLocation,
    String dropoffLocation,
    String status,
    LocalDateTime scheduledAt,
    LocalDateTime startedAt,
    LocalDateTime completedAt
) {}
