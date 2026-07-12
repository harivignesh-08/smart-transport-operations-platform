package com.quiz.trip_service.client;

import com.quiz.trip_service.dto.TrackingResponse;
import com.quiz.trip_service.dto.TrackingStartRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "tracking-service")
public interface TrackingClient {

    @PostMapping("/api/tracking")
    TrackingResponse startTracking(
            @RequestHeader("Authorization") String token,
            @RequestBody TrackingStartRequest request);

    @PutMapping("/api/tracking/{tripId}/stop")
    TrackingResponse stopTracking(
            @RequestHeader("Authorization") String token,
            @PathVariable("tripId") Long tripId);
}
