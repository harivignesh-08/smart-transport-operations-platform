package com.quiz.trip_service.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
    Long id,
    Long tripId,
    String recipientType,
    String recipientId,
    String eventType,
    String message,
    String status,
    LocalDateTime createdAt
) {}
