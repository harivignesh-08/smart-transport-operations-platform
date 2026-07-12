package com.quiz.maintenance_service.dto;

public record MaintenancePredictionRequest(
    String vehicleId,
    double totalDistance,
    double engineHours,
    double fuelConsumption,
    String lastServiceDate
) {}
