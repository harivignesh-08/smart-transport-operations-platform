package com.quiz.driver_service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LicenseResponse(
    Long id,
    String licenseNumber,
    String licenseClass,
    LocalDate issueDate,
    LocalDate expiryDate,
    String fileUrl,
    Long driverId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
