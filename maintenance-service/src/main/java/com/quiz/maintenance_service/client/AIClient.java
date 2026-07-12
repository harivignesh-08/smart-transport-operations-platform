package com.quiz.maintenance_service.client;

import com.quiz.maintenance_service.dto.MaintenancePredictionRequest;
import com.quiz.maintenance_service.dto.MaintenancePredictionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service", url = "${transitops.ai-service.url}")
public interface AIClient {

    @PostMapping("/predict/maintenance")
    MaintenancePredictionResponse predictMaintenance(@RequestBody MaintenancePredictionRequest request);
}
