package com.quiz.tracking_service.dto;

public record RouteAnalysisRequest(
    Long tripId,
    Long vehicleId,
    int pointCount,
    double totalDistanceKm,
    double averageSpeedKmh
) {}
