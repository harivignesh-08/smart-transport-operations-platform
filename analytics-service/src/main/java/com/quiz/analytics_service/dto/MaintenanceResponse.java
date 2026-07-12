package com.quiz.analytics_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MaintenanceResponse(
    Long id,
    Long vehicleId,
    String description,
    String status,
    BigDecimal cost,
    LocalDateTime maintenanceDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
