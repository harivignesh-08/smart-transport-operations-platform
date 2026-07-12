package com.quiz.notification_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MaintenanceAlertRequest(
    @NotNull Long vehicleId,
    @NotBlank String maintenanceType,
    @NotBlank String description
) {}
