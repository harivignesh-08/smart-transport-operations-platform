package com.quiz.analytics_service.client;

import com.quiz.analytics_service.dto.TrackingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "tracking-service")
public interface TrackingClient {

    @GetMapping("/api/tracking/vehicle/{vehicleId}")
    List<TrackingResponse> getVehicleLocations(@RequestHeader("Authorization") String token, @PathVariable("vehicleId") Long vehicleId);

    @GetMapping("/api/tracking/active")
    List<TrackingResponse> getAllActiveLocations(@RequestHeader("Authorization") String token);
}
