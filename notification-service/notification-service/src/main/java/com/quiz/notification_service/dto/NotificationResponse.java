package com.quiz.notification_service.dto;

import com.quiz.notification_service.entity.NotificationStatus;
import com.quiz.notification_service.entity.NotificationType;
import java.time.LocalDateTime;

public record NotificationResponse(
    Long id,
    NotificationType type,
    String recipient,
    String subject,
    String message,
    NotificationStatus status,
    Long relatedEntityId,
    String relatedEntityType,
    LocalDateTime createdAt,
    LocalDateTime sentAt
) {}
