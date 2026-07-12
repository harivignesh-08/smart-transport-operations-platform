package com.quiz.maintenance_service.dto;

import java.time.LocalDateTime;

public record VehicleResponse(
    Long id,
    String licensePlate,
    String make,
    String model,
    Integer year,
    VehicleStatus status,
    AvailabilityStatus availabilityStatus,
    Long driverId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
