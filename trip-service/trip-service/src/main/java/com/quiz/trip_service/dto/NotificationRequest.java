package com.quiz.trip_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequest(
    @NotNull Long tripId,
    @NotBlank String recipientType,
    @NotBlank String recipientId,
    @NotBlank String eventType,
    @NotBlank String message
) {}
