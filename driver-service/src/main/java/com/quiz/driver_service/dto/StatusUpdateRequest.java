package com.quiz.driver_service.dto;

import jakarta.validation.constraints.NotBlank;

public record StatusUpdateRequest(
    @NotBlank(message = "Status is required")
    String status
) {}
