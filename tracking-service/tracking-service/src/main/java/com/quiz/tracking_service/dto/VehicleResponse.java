package com.quiz.tracking_service.dto;

import java.time.LocalDateTime;

public record VehicleResponse(
    Long id,
    String licensePlate,
    String make,
    String model,
    Integer year,
    String status,
    String availabilityStatus,
    Long driverId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
