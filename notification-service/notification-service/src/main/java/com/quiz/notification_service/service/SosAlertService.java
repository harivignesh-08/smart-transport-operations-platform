package com.quiz.notification_service.service;

import com.quiz.notification_service.client.DriverClient;
import com.quiz.notification_service.dto.*;
import com.quiz.notification_service.entity.Notification;
import com.quiz.notification_service.entity.NotificationStatus;
import com.quiz.notification_service.entity.NotificationType;
import com.quiz.notification_service.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SosAlertService {

    private static final Logger log = LoggerFactory.getLogger(SosAlertService.class);

    private final DriverClient driverClient;
    private final EmailService emailService;
    private final SmsService smsService;
    private final NotificationRepository notificationRepository;

    public SosAlertService(DriverClient driverClient,
                           EmailService emailService,
                           SmsService smsService,
                           NotificationRepository notificationRepository) {
        this.driverClient = driverClient;
        this.emailService = emailService;
        this.smsService = smsService;
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationResponse> sendSosAlert(SosAlertRequest request, String authToken) {
        DriverResponse driver = driverClient.getDriverById(authToken, request.driverId());

        String subject = "SOS ALERT - Driver " + driver.firstName() + " " + driver.lastName();
        String locationInfo = request.latitude() != null && request.longitude() != null
            ? String.format("Location: (%.6f, %.6f). ", request.latitude(), request.longitude())
            : "";
        String userMessage = request.message() != null ? request.message() : "Emergency assistance required";
        String message = String.format(
            "SOS ALERT! Driver %s %s (ID: %d) needs immediate assistance. %s%s",
            driver.firstName(),
            driver.lastName(),
            driver.id(),
            locationInfo,
            userMessage
        );

        List<NotificationResponse> responses = new ArrayList<>();

        Notification sosNotification = new Notification();
        sosNotification.setType(NotificationType.SOS_ALERT);
        sosNotification.setRecipient(driver.email() != null ? driver.email() : driver.phone());
        sosNotification.setSubject(subject);
        sosNotification.setMessage(message);
        sosNotification.setRelatedEntityId(driver.id());
        sosNotification.setRelatedEntityType("DRIVER");
        sosNotification.setStatus(NotificationStatus.SENT);
        sosNotification.setSentAt(LocalDateTime.now());
        responses.add(toResponse(notificationRepository.save(sosNotification)));

        if (driver.phone() != null) {
            responses.add(smsService.sendSms(new SmsRequest(driver.phone(), message)));
        }
        if (driver.email() != null) {
            responses.add(emailService.sendEmail(new EmailRequest(driver.email(), subject, message)));
        }

        log.warn("SOS alert triggered for driver {} at ({}, {})",
            driver.id(), request.latitude(), request.longitude());
        return responses;
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
