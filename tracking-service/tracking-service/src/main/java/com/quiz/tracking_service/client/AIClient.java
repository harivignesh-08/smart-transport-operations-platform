package com.quiz.tracking_service.client;

import com.quiz.tracking_service.dto.RouteAnalysisRequest;
import com.quiz.tracking_service.dto.RouteAnalysisResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ai-service")
public interface AIClient {

    @PostMapping("/api/ai/routes/analyze")
    RouteAnalysisResponse analyzeRoute(
            @RequestHeader("Authorization") String token,
            @RequestBody RouteAnalysisRequest request);
}
