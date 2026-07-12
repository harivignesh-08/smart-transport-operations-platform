package com.quiz.vehicle_service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DocumentResponse(
    Long id,
    String documentType,
    String documentNumber,
    LocalDate expiryDate,
    String fileUrl,
    Long vehicleId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
