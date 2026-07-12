package com.quiz.analytics_service.dto;

import java.time.LocalDateTime;

public record PerformanceResponse(
    Long id,
    Long driverId,
    Integer totalTrips,
    Integer completedTrips,
    Integer cancelledTrips,
    Double averageRating,
    Double onTimePercentage,
    Double totalDistanceKm,
    LocalDateTime lastUpdated
) {}
