package com.quiz.report_service.dto;

import java.math.BigDecimal;

public record DashboardResponse(
    long totalVehicles,
    long availableVehicles,
    long activeVehicles,
    long totalDrivers,
    long availableDrivers,
    long activeTripsCount,
    BigDecimal totalFuelCost,
    BigDecimal totalMaintenanceCost,
    BigDecimal totalExpenses
) {}
