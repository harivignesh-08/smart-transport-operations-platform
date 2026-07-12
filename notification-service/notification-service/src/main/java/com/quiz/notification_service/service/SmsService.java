package com.quiz.notification_service.service;

import com.quiz.notification_service.dto.NotificationResponse;
import com.quiz.notification_service.dto.SmsRequest;
import com.quiz.notification_service.entity.Notification;
import com.quiz.notification_service.entity.NotificationStatus;
import com.quiz.notification_service.entity.NotificationType;
import com.quiz.notification_service.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SmsService {

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);

    private final NotificationRepository notificationRepository;

    @Value("${notification.sms.enabled:false}")
    private boolean smsEnabled;

    public SmsService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationResponse sendSms(SmsRequest request) {
        Notification notification = new Notification();
        notification.setType(NotificationType.SMS);
        notification.setRecipient(request.phoneNumber());
        notification.setSubject("SMS Notification");
        notification.setMessage(request.message());
        notification.setStatus(NotificationStatus.PENDING);

        notification = notificationRepository.save(notification);

        try {
            if (smsEnabled) {
                log.info("SMS sent to {}: {}", request.phoneNumber(), request.message());
            } else {
                log.info("SMS (mock): to={}, message={}", request.phoneNumber(), request.message());
            }

            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
        } catch (Exception e) {
            log.error("Failed to send SMS to {}", request.phoneNumber(), e);
            notification.setStatus(NotificationStatus.FAILED);
        }

        return toResponse(notificationRepository.save(notification));
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
