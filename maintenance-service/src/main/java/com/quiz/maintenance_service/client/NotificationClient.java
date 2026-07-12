package com.quiz.maintenance_service.client;

import com.quiz.maintenance_service.dto.NotificationRequest;
import com.quiz.maintenance_service.dto.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/api/notifications")
    NotificationResponse sendNotification(@RequestBody NotificationRequest request);
}
