package com.quiz.maintenance_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record MaintenanceScheduleRequest(
    @NotNull(message = "Vehicle ID is required")
    Long vehicleId,

    @NotBlank(message = "Service type is required")
    String serviceType,

    @NotNull(message = "Scheduled date is required")
    LocalDate scheduledDate,

    String notes
) {}
