package com.quiz.notification_service.service;

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
public class MaintenanceAlertService {

    private static final Logger log = LoggerFactory.getLogger(MaintenanceAlertService.class);

    private final NotificationRepository notificationRepository;

    public MaintenanceAlertService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationResponse> sendMaintenanceAlert(MaintenanceAlertRequest request) {
        String subject = "Maintenance Alert - Vehicle #" + request.vehicleId();
        String message = String.format(
            "Maintenance required for Vehicle #%d. Type: %s. Details: %s",
            request.vehicleId(),
            request.maintenanceType(),
            request.description()
        );

        List<NotificationResponse> responses = new ArrayList<>();

        Notification maintenanceNotification = new Notification();
        maintenanceNotification.setType(NotificationType.MAINTENANCE_ALERT);
        maintenanceNotification.setRecipient("fleet-operations");
        maintenanceNotification.setSubject(subject);
        maintenanceNotification.setMessage(message);
        maintenanceNotification.setRelatedEntityId(request.vehicleId());
        maintenanceNotification.setRelatedEntityType("VEHICLE");
        maintenanceNotification.setStatus(NotificationStatus.SENT);
        maintenanceNotification.setSentAt(LocalDateTime.now());
        responses.add(toResponse(notificationRepository.save(maintenanceNotification)));

        log.info("Maintenance alert created for vehicle {}: {}", request.vehicleId(), request.maintenanceType());
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
