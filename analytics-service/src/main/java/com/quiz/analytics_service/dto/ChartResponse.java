package com.quiz.analytics_service.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ChartResponse(
    Map<String, BigDecimal> monthlyExpenses,
    Map<String, Long> tripsByStatus,
    Map<String, Double> fuelEfficiencyTrend,
    Map<String, BigDecimal> expensesByCategory
) {}
