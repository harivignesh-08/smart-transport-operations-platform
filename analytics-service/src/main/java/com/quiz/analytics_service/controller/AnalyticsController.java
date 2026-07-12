package com.quiz.analytics_service.controller;

import com.quiz.analytics_service.dto.ChartResponse;
import com.quiz.analytics_service.dto.DashboardResponse;
import com.quiz.analytics_service.dto.KPIsResponse;
import com.quiz.analytics_service.dto.StatisticsResponse;
import com.quiz.analytics_service.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<DashboardResponse> getDashboard(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(analyticsService.getDashboardData(token));
    }

    @GetMapping("/charts")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<ChartResponse> getCharts(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(analyticsService.getChartData(token));
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<StatisticsResponse> getStatistics(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(analyticsService.getStatisticsData(token));
    }

    @GetMapping("/kpis")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<KPIsResponse> getKPIs(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(analyticsService.getKPIData(token));
    }
}
