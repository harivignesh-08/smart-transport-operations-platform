package com.quiz.report_service.dto;

import java.math.BigDecimal;

public record StatisticsResponse(
    long totalTrips,
    double totalDistanceKm,
    double averageDistanceKm,
    BigDecimal averageFuelCostPerLiter,
    BigDecimal averageExpenseAmount,
    double averageSpeed
) {}
