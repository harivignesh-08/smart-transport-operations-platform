package com.quiz.notification_service.repository;

import com.quiz.notification_service.entity.Notification;
import com.quiz.notification_service.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByType(NotificationType type);

    List<Notification> findByRelatedEntityIdAndRelatedEntityType(Long relatedEntityId, String relatedEntityType);
}
