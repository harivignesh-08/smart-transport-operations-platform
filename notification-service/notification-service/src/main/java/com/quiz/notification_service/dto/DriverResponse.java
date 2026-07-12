package com.quiz.notification_service.dto;

import java.time.LocalDateTime;

public record DriverResponse(
    Long id,
    Long userId,
    String firstName,
    String lastName,
    String phone,
    String email,
    String status,
    String availabilityStatus,
    Long vehicleId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
