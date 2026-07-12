package com.quiz.driver_service.dto;

import com.quiz.driver_service.entity.AvailabilityStatus;
import com.quiz.driver_service.entity.DriverStatus;
import java.time.LocalDateTime;

public record DriverResponse(
    Long id,
    Long userId,
    String firstName,
    String lastName,
    String phone,
    String email,
    DriverStatus status,
    AvailabilityStatus availabilityStatus,
    Long vehicleId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
