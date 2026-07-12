package com.quiz.trip_service.dto;

public record PerformanceUpdateRequest(
    Integer completedTripsDelta,
    Integer cancelledTripsDelta,
    Double tripRating,
    Boolean onTime,
    Double distanceKmDelta
) {}
