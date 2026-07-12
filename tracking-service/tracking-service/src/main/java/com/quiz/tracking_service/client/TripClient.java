package com.quiz.tracking_service.client;

import com.quiz.tracking_service.dto.TripResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "trip-service")
public interface TripClient {

    @GetMapping("/api/trips/{id}")
    TripResponse getTripById(@RequestHeader("Authorization") String token, @PathVariable("id") Long id);
}
