package com.quiz.notification_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TripAlertRequest(
    @NotNull Long tripId,
    @NotBlank String alertMessage
) {}
