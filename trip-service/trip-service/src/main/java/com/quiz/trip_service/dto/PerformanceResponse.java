package com.quiz.trip_service.dto;

import java.time.LocalDateTime;

public record PerformanceResponse(
    Long id,
    Long driverId,
    Integer totalTrips,
    Integer completedTrips,
    Integer cancelledTrips,
    Double averageRating,
    Double totalDistanceKm,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
