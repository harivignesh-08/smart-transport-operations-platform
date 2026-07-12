package com.quiz.notification_service.service;

import com.quiz.notification_service.client.DriverClient;
import com.quiz.notification_service.client.TripClient;
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
public class TripAlertService {

    private static final Logger log = LoggerFactory.getLogger(TripAlertService.class);

    private final TripClient tripClient;
    private final DriverClient driverClient;
    private final EmailService emailService;
    private final SmsService smsService;
    private final NotificationRepository notificationRepository;

    public TripAlertService(TripClient tripClient,
                            DriverClient driverClient,
                            EmailService emailService,
                            SmsService smsService,
                            NotificationRepository notificationRepository) {
        this.tripClient = tripClient;
        this.driverClient = driverClient;
        this.emailService = emailService;
        this.smsService = smsService;
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationResponse> sendTripAlert(TripAlertRequest request, String authToken) {
        TripResponse trip = tripClient.getTripById(authToken, request.tripId());
        DriverResponse driver = driverClient.getDriverById(authToken, trip.driverId());

        String subject = "Trip Alert - Trip #" + trip.id();
        String message = String.format(
            "Trip Alert for Trip #%d (%s -> %s). Status: %s. %s",
            trip.id(),
            trip.pickupLocation(),
            trip.dropoffLocation(),
            trip.status(),
            request.alertMessage()
        );

        List<NotificationResponse> responses = new ArrayList<>();

        Notification alertNotification = createAlertNotification(
            NotificationType.TRIP_ALERT,
            driver.email(),
            subject,
            message,
            trip.id(),
            "TRIP"
        );
        responses.add(toResponse(alertNotification));

        if (driver.email() != null) {
            responses.add(emailService.sendEmail(new EmailRequest(driver.email(), subject, message)));
        }
        if (driver.phone() != null) {
            responses.add(smsService.sendSms(new SmsRequest(driver.phone(), message)));
        }

        log.info("Trip alert sent for trip {} to driver {}", trip.id(), driver.id());
        return responses;
    }

    private Notification createAlertNotification(NotificationType type, String recipient,
                                                  String subject, String message,
                                                  Long relatedEntityId, String relatedEntityType) {
        Notification notification = new Notification();
        notification.setType(type);
        notification.setRecipient(recipient);
        notification.setSubject(subject);
        notification.setMessage(message);
        notification.setRelatedEntityId(relatedEntityId);
        notification.setRelatedEntityType(relatedEntityType);
        notification.setStatus(NotificationStatus.SENT);
        notification.setSentAt(LocalDateTime.now());
        return notificationRepository.save(notification);
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
