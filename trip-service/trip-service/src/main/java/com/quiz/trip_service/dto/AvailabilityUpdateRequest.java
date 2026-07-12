package com.quiz.trip_service.dto;

public record AvailabilityUpdateRequest(
    String availabilityStatus,
    Long driverId
) {}
