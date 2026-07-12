package com.quiz.trip_service.dto;

import com.quiz.trip_service.entity.TripStatus;
import java.time.LocalDateTime;

public record TripResponse(
    Long id,
    String origin,
    String destination,
    Long vehicleId,
    Long driverId,
    TripStatus status,
    Double estimatedDistanceKm,
    Double estimatedDurationMinutes,
    Double actualDistanceKm,
    LocalDateTime startedAt,
    LocalDateTime endedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
