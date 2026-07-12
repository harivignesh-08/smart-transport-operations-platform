package com.quiz.report_service.client;

import com.quiz.report_service.dto.DashboardResponse;
import com.quiz.report_service.dto.KPIsResponse;
import com.quiz.report_service.dto.StatisticsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "analytics-service")
public interface AnalyticsClient {

    @GetMapping("/api/analytics/dashboard")
    DashboardResponse getDashboard(@RequestHeader("Authorization") String token);

    @GetMapping("/api/analytics/statistics")
    StatisticsResponse getStatistics(@RequestHeader("Authorization") String token);

    @GetMapping("/api/analytics/kpis")
    KPIsResponse getKPIs(@RequestHeader("Authorization") String token);
}
