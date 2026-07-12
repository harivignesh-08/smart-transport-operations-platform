package com.quiz.notification_service.client;

import com.quiz.notification_service.dto.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "driver-service")
public interface DriverClient {

    @GetMapping("/api/drivers/{id}")
    DriverResponse getDriverById(@RequestHeader("Authorization") String token,
                                 @PathVariable("id") Long id);

    @GetMapping("/api/drivers/user/{userId}")
    DriverResponse getDriverByUserId(@RequestHeader("Authorization") String token,
                                     @PathVariable("userId") Long userId);
}
