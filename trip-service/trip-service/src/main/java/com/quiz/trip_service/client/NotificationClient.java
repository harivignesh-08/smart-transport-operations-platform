package com.quiz.trip_service.client;

import com.quiz.trip_service.dto.NotificationRequest;
import com.quiz.trip_service.dto.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/api/notifications")
    NotificationResponse sendNotification(
            @RequestHeader("Authorization") String token,
            @RequestBody NotificationRequest request);
}
