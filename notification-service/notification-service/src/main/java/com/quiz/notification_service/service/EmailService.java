package com.quiz.notification_service.service;

import com.quiz.notification_service.dto.EmailRequest;
import com.quiz.notification_service.dto.NotificationResponse;
import com.quiz.notification_service.entity.Notification;
import com.quiz.notification_service.entity.NotificationStatus;
import com.quiz.notification_service.entity.NotificationType;
import com.quiz.notification_service.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    public EmailService(JavaMailSender mailSender, NotificationRepository notificationRepository) {
        this.mailSender = mailSender;
        this.notificationRepository = notificationRepository;
    }

    public NotificationResponse sendEmail(EmailRequest request) {
        Notification notification = new Notification();
        notification.setType(NotificationType.EMAIL);
        notification.setRecipient(request.recipient());
        notification.setSubject(request.subject());
        notification.setMessage(request.message());
        notification.setStatus(NotificationStatus.PENDING);

        notification = notificationRepository.save(notification);

        try {
            if (StringUtils.hasText(mailUsername)) {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(request.recipient());
                mailMessage.setSubject(request.subject());
                mailMessage.setText(request.message());
                mailSender.send(mailMessage);
            } else {
                log.info("Email (mock): to={}, subject={}", request.recipient(), request.subject());
            }

            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
        } catch (Exception e) {
            log.error("Failed to send email to {}", request.recipient(), e);
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
