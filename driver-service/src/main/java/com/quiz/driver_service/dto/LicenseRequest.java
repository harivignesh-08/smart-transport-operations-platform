package com.quiz.driver_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record LicenseRequest(
    @NotBlank(message = "License number is required")
    String licenseNumber,

    @NotBlank(message = "License class is required")
    String licenseClass,

    @NotNull(message = "Issue date is required")
    LocalDate issueDate,

    @NotNull(message = "Expiry date is required")
    LocalDate expiryDate,

    String fileUrl
) {}
