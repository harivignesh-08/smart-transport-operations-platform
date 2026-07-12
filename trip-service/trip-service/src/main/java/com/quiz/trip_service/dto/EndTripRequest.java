package com.quiz.trip_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record EndTripRequest(
    Double actualDistanceKm,
    @Min(1) @Max(5) Double tripRating,
    Boolean onTime
) {}
