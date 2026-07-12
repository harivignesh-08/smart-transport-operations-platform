package com.quiz.analytics_service.service;

import com.quiz.analytics_service.dto.ChartResponse;
import com.quiz.analytics_service.dto.DashboardResponse;
import com.quiz.analytics_service.dto.KPIsResponse;
import com.quiz.analytics_service.dto.StatisticsResponse;

public interface AnalyticsService {

    DashboardResponse getDashboardData(String token);

    ChartResponse getChartData(String token);

    StatisticsResponse getStatisticsData(String token);

    KPIsResponse getKPIData(String token);
}
