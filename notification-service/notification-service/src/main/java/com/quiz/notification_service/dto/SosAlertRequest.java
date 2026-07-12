package com.quiz.notification_service.dto;

import jakarta.validation.constraints.NotNull;

public record SosAlertRequest(
    @NotNull Long driverId,
    Double latitude,
    Double longitude,
    String message
) {}
