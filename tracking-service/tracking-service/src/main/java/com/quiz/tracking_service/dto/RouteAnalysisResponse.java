package com.quiz.tracking_service.dto;

public record RouteAnalysisResponse(
    String summary,
    Double efficiencyScore,
    String recommendation
) {}
