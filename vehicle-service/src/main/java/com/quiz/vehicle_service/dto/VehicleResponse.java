package com.quiz.vehicle_service.dto;

import com.quiz.vehicle_service.entity.AvailabilityStatus;
import com.quiz.vehicle_service.entity.VehicleStatus;
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
