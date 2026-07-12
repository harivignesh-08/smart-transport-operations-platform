package com.quiz.driver_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PerformanceUpdateRequest(
    Integer completedTripsDelta,
    Integer cancelledTripsDelta,
    @Min(1) @Max(5) Double tripRating,
    Boolean onTime,
    Double distanceKmDelta
) {}
