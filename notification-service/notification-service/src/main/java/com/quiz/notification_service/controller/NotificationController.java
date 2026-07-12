package com.quiz.notification_service.controller;

import com.quiz.notification_service.dto.*;
import com.quiz.notification_service.entity.NotificationType;
import com.quiz.notification_service.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final EmailService emailService;
    private final SmsService smsService;
    private final TripAlertService tripAlertService;
    private final SosAlertService sosAlertService;
    private final MaintenanceAlertService maintenanceAlertService;

    public NotificationController(NotificationService notificationService,
                                  EmailService emailService,
                                  SmsService smsService,
                                  TripAlertService tripAlertService,
                                  SosAlertService sosAlertService,
                                  MaintenanceAlertService maintenanceAlertService) {
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.smsService = smsService;
        this.tripAlertService = tripAlertService;
        this.sosAlertService = sosAlertService;
        this.maintenanceAlertService = maintenanceAlertService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByType(@PathVariable NotificationType type) {
        return ResponseEntity.ok(notificationService.getNotificationsByType(type));
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        return ResponseEntity.ok(notificationService.getNotificationsByEntity(entityId, entityType));
    }

    @PostMapping("/email")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<NotificationResponse> sendEmail(@Valid @RequestBody EmailRequest request) {
        NotificationResponse response = emailService.sendEmail(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/sms")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<NotificationResponse> sendSms(@Valid @RequestBody SmsRequest request) {
        NotificationResponse response = smsService.sendSms(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/trip-alert")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<NotificationResponse>> sendTripAlert(
            @Valid @RequestBody TripAlertRequest request,
            HttpServletRequest httpRequest) {
        List<NotificationResponse> responses = tripAlertService.sendTripAlert(request, extractAuthToken(httpRequest));
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }

    @PostMapping("/sos-alert")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<NotificationResponse>> sendSosAlert(
            @Valid @RequestBody SosAlertRequest request,
            HttpServletRequest httpRequest) {
        List<NotificationResponse> responses = sosAlertService.sendSosAlert(request, extractAuthToken(httpRequest));
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }

    @PostMapping("/maintenance-alert")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> sendMaintenanceAlert(
            @Valid @RequestBody MaintenanceAlertRequest request) {
        List<NotificationResponse> responses = maintenanceAlertService.sendMaintenanceAlert(request);
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }

    private String extractAuthToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header;
        }
        throw new IllegalArgumentException("Missing or invalid Authorization header");
    }
}
