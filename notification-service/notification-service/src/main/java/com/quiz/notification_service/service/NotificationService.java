package com.quiz.notification_service.service;

import com.quiz.notification_service.dto.NotificationResponse;
import com.quiz.notification_service.entity.Notification;
import com.quiz.notification_service.entity.NotificationType;
import com.quiz.notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    public NotificationResponse getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + id));
        return toResponse(notification);
    }

    public List<NotificationResponse> getNotificationsByType(NotificationType type) {
        return notificationRepository.findByType(type).stream()
            .map(this::toResponse)
            .toList();
    }

    public List<NotificationResponse> getNotificationsByEntity(Long entityId, String entityType) {
        return notificationRepository.findByRelatedEntityIdAndRelatedEntityType(entityId, entityType).stream()
            .map(this::toResponse)
            .toList();
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
            notification.getId(),
            notification.getType(),
            notification.getRecipient(),
            notification.getSubject(),
            notification.getMessage(),
            notification.getStatus(),
            notification.getRelatedEntityId(),
            notification.getRelatedEntityType(),
            notification.getCreatedAt(),
            notification.getSentAt()
        );
    }
}
