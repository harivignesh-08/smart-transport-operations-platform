package com.quiz.analytics_service.dto;

public record KPIsResponse(
    double fleetUtilizationRate,
    double averageDriverRating,
    double averageFuelEfficiency,
    double costPerKm,
    double safetyScore
) {}
