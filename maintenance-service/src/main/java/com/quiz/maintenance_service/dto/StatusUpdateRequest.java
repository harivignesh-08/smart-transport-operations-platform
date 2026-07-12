package com.quiz.maintenance_service.dto;

import jakarta.validation.constraints.NotBlank;

public record StatusUpdateRequest(
    @NotBlank(message = "Status is required")
    String status
) {}
