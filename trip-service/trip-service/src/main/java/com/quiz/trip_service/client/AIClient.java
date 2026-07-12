package com.quiz.trip_service.client;

import com.quiz.trip_service.dto.RouteRequest;
import com.quiz.trip_service.dto.RouteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service", url = "${transitops.ai-service.url}")
public interface AIClient {

    @PostMapping("/predict/route")
    RouteResponse optimizeRoute(@RequestBody RouteRequest request);
}
