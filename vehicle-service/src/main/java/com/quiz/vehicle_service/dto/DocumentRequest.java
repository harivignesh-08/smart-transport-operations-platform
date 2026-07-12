package com.quiz.vehicle_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record DocumentRequest(
    @NotBlank(message = "Document type is required")
    String documentType,

    @NotBlank(message = "Document number is required")
    String documentNumber,

    @NotNull(message = "Expiry date is required")
    LocalDate expiryDate,

    String fileUrl
) {}
