package com.quiz.maintenance_service.dto;

public record NotificationRequest(
    Long tripId,
    String recipientType,
    String recipientId,
    String eventType,
    String message
) {}
