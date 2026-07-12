package com.quiz.maintenance_service.dto;

import java.util.List;

public record MaintenancePredictionResponse(
    boolean maintenanceRequired,
    double maintenanceScore,
    List<String> suggestedMaintenance
) {}
