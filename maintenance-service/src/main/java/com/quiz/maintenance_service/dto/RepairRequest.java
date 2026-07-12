package com.quiz.maintenance_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RepairRequest(
    @NotNull(message = "Vehicle ID is required")
    Long vehicleId,

    @NotBlank(message = "Description is required")
    String description,

    BigDecimal cost,

    @NotNull(message = "Start date is required")
    LocalDate startDate,

    LocalDate endDate,

    String status
) {}
