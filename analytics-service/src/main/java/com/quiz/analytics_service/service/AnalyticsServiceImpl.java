package com.quiz.analytics_service.service;

import com.quiz.analytics_service.client.*;
import com.quiz.analytics_service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    private final VehicleClient vehicleClient;
    private final DriverClient driverClient;
    private final TripClient tripClient;
    private final TrackingClient trackingClient;
    private final FuelClient fuelClient;
    private final MaintenanceClient maintenanceClient;

    public AnalyticsServiceImpl(VehicleClient vehicleClient, DriverClient driverClient,
                                TripClient tripClient, TrackingClient trackingClient,
                                FuelClient fuelClient, MaintenanceClient maintenanceClient) {
        this.vehicleClient = vehicleClient;
        this.driverClient = driverClient;
        this.tripClient = tripClient;
        this.trackingClient = trackingClient;
        this.fuelClient = fuelClient;
        this.maintenanceClient = maintenanceClient;
    }

    private List<VehicleResponse> safeGetVehicles(String token) {
        try {
            return vehicleClient.getAllVehicles(token);
        } catch (Exception e) {
            log.warn("Failed to fetch vehicles from vehicle-service, using fallback empty list: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<DriverResponse> safeGetDrivers(String token) {
        try {
            return driverClient.getAllDrivers(token);
        } catch (Exception e) {
            log.warn("Failed to fetch drivers from driver-service, using fallback empty list: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<TripResponse> safeGetTrips(String token) {
        try {
            return tripClient.getAllTrips(token);
        } catch (Exception e) {
            log.warn("Failed to fetch trips from trip-service, using fallback empty list: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<TrackingResponse> safeGetTracking(String token) {
        try {
            return trackingClient.getAllActiveLocations(token);
        } catch (Exception e) {
            log.warn("Failed to fetch tracking data from tracking-service, using fallback empty list: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<FuelLogResponse> safeGetFuelLogs(String token) {
        try {
            return fuelClient.getAllFuelLogs(token);
        } catch (Exception e) {
            log.warn("Failed to fetch fuel logs from fuel-expense-service, using fallback empty list: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<ExpenseResponse> safeGetExpenses(String token) {
        try {
            return fuelClient.getAllExpenses(token);
        } catch (Exception e) {
            log.warn("Failed to fetch expenses from fuel-expense-service, using fallback empty list: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<MaintenanceResponse> safeGetMaintenance(String token) {
        try {
            return maintenanceClient.getAllMaintenance(token);
        } catch (Exception e) {
            log.warn("Failed to fetch maintenance from maintenance-service, using fallback empty list: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private Double safeGetDriverRating(String token, Long driverId) {
        try {
            PerformanceResponse perf = driverClient.getPerformanceByDriver(token, driverId);
            return perf != null ? perf.averageRating() : null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public DashboardResponse getDashboardData(String token) {
        List<VehicleResponse> vehicles = safeGetVehicles(token);
        List<DriverResponse> drivers = safeGetDrivers(token);
        List<TripResponse> trips = safeGetTrips(token);
        List<FuelLogResponse> fuelLogs = safeGetFuelLogs(token);
        List<ExpenseResponse> expenses = safeGetExpenses(token);
        List<MaintenanceResponse> maintenanceLogs = safeGetMaintenance(token);

        long totalVehicles = vehicles.size();
        long availableVehicles = vehicles.stream()
                .filter(v -> "AVAILABLE".equalsIgnoreCase(v.availabilityStatus()) || "AVAILABLE".equalsIgnoreCase(v.status()))
                .count();
        long activeVehicles = vehicles.stream()
                .filter(v -> "ON_TRIP".equalsIgnoreCase(v.availabilityStatus()) || "ACTIVE".equalsIgnoreCase(v.status()))
                .count();

        long totalDrivers = drivers.size();
        long availableDrivers = drivers.stream()
                .filter(d -> "AVAILABLE".equalsIgnoreCase(d.availabilityStatus()))
                .count();

        long activeTrips = trips.stream()
                .filter(t -> "ACTIVE".equalsIgnoreCase(t.status()) || "ON_TRIP".equalsIgnoreCase(t.status()) || "IN_PROGRESS".equalsIgnoreCase(t.status()))
                .count();

        BigDecimal totalFuelCost = fuelLogs.stream()
                .map(FuelLogResponse::totalCost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalMaintenanceCost = maintenanceLogs.stream()
                .map(MaintenanceResponse::cost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = expenses.stream()
                .map(ExpenseResponse::amount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardResponse(
                totalVehicles,
                availableVehicles,
                activeVehicles,
                totalDrivers,
                availableDrivers,
                activeTrips,
                totalFuelCost,
                totalMaintenanceCost,
                totalExpenses
        );
    }

    @Override
    public ChartResponse getChartData(String token) {
        List<TripResponse> trips = safeGetTrips(token);
        List<FuelLogResponse> fuelLogs = safeGetFuelLogs(token);
        List<ExpenseResponse> expenses = safeGetExpenses(token);

        // Group expenses by category
        Map<String, BigDecimal> expensesByCategory = new HashMap<>();
        for (ExpenseResponse exp : expenses) {
            String cat = exp.category() != null ? exp.category() : "OTHER";
            BigDecimal amt = exp.amount() != null ? exp.amount() : BigDecimal.ZERO;
            expensesByCategory.put(cat, expensesByCategory.getOrDefault(cat, BigDecimal.ZERO).add(amt));
        }

        // Group trips by status
        Map<String, Long> tripsByStatus = new HashMap<>();
        for (TripResponse trip : trips) {
            String status = trip.status() != null ? trip.status().toUpperCase() : "UNKNOWN";
            tripsByStatus.put(status, tripsByStatus.getOrDefault(status, 0L) + 1);
        }

        // Monthly expenses (combining Expenses, Fuel, and Maintenance)
        Map<String, BigDecimal> monthlyExpenses = new TreeMap<>();
        for (ExpenseResponse exp : expenses) {
            if (exp.expenseDate() != null) {
                String month = exp.expenseDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
                BigDecimal amt = exp.amount() != null ? exp.amount() : BigDecimal.ZERO;
                monthlyExpenses.put(month, monthlyExpenses.getOrDefault(month, BigDecimal.ZERO).add(amt));
            }
        }
        for (FuelLogResponse log : fuelLogs) {
            if (log.filledAt() != null) {
                String month = log.filledAt().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
                BigDecimal amt = log.totalCost() != null ? log.totalCost() : BigDecimal.ZERO;
                monthlyExpenses.put(month, monthlyExpenses.getOrDefault(month, BigDecimal.ZERO).add(amt));
            }
        }

        // Fuel efficiency trend (Month -> Avg efficiency: distance/quantity)
        Map<String, Double> fuelEfficiencyTrend = new TreeMap<>();
        Map<String, Double> monthlyDistance = new HashMap<>();
        Map<String, Double> monthlyLiters = new HashMap<>();

        for (FuelLogResponse fuel : fuelLogs) {
            if (fuel.filledAt() != null && fuel.quantityLiters() != null) {
                String month = fuel.filledAt().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
                monthlyLiters.put(month, monthlyLiters.getOrDefault(month, 0.0) + fuel.quantityLiters().doubleValue());
            }
        }

        for (TripResponse trip : trips) {
            if (trip.startTime() != null && trip.distanceKm() != null) {
                String month = trip.startTime().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
                monthlyDistance.put(month, monthlyDistance.getOrDefault(month, 0.0) + trip.distanceKm().doubleValue());
            }
        }

        for (String month : monthlyLiters.keySet()) {
            double liters = monthlyLiters.get(month);
            double distance = monthlyDistance.getOrDefault(month, 0.0);
            if (liters > 0) {
                // Efficiency as km/L
                fuelEfficiencyTrend.put(month, Math.round((distance / liters) * 100.0) / 100.0);
            }
        }

        return new ChartResponse(
                monthlyExpenses,
                tripsByStatus,
                fuelEfficiencyTrend,
                expensesByCategory
        );
    }

    @Override
    public StatisticsResponse getStatisticsData(String token) {
        List<TripResponse> trips = safeGetTrips(token);
        List<FuelLogResponse> fuelLogs = safeGetFuelLogs(token);
        List<ExpenseResponse> expenses = safeGetExpenses(token);
        List<TrackingResponse> locations = safeGetTracking(token);

        long totalTrips = trips.size();
        double totalDistance = trips.stream()
                .map(TripResponse::distanceKm)
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();

        double avgDistance = totalTrips > 0 ? totalDistance / totalTrips : 0.0;

        BigDecimal avgFuelCostPerLiter = BigDecimal.ZERO;
        if (!fuelLogs.isEmpty()) {
            BigDecimal totalFuelPrice = fuelLogs.stream()
                    .map(FuelLogResponse::costPerLiter)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            avgFuelCostPerLiter = totalFuelPrice.divide(BigDecimal.valueOf(fuelLogs.size()), 2, RoundingMode.HALF_UP);
        }

        BigDecimal avgExpenseAmount = BigDecimal.ZERO;
        if (!expenses.isEmpty()) {
            BigDecimal totalExpenseAmount = expenses.stream()
                    .map(ExpenseResponse::amount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            avgExpenseAmount = totalExpenseAmount.divide(BigDecimal.valueOf(expenses.size()), 2, RoundingMode.HALF_UP);
        }

        double avgSpeed = 0.0;
        if (!locations.isEmpty()) {
            avgSpeed = locations.stream()
                    .map(TrackingResponse::speed)
                    .filter(Objects::nonNull)
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
        }

        return new StatisticsResponse(
                totalTrips,
                totalDistance,
                avgDistance,
                avgFuelCostPerLiter,
                avgExpenseAmount,
                avgSpeed
        );
    }

    @Override
    public KPIsResponse getKPIData(String token) {
        List<VehicleResponse> vehicles = safeGetVehicles(token);
        List<DriverResponse> drivers = safeGetDrivers(token);
        List<TripResponse> trips = safeGetTrips(token);
        List<FuelLogResponse> fuelLogs = safeGetFuelLogs(token);
        List<ExpenseResponse> expenses = safeGetExpenses(token);
        List<MaintenanceResponse> maintenanceLogs = safeGetMaintenance(token);

        // 1. Fleet Utilization
        long totalVehicles = vehicles.size();
        long activeVehicles = vehicles.stream()
                .filter(v -> "ON_TRIP".equalsIgnoreCase(v.availabilityStatus()) || "ACTIVE".equalsIgnoreCase(v.status()))
                .count();
        double fleetUtilization = totalVehicles > 0 ? (activeVehicles / (double) totalVehicles) * 100.0 : 0.0;

        // 2. Average Driver Rating
        double totalRatings = 0.0;
        int ratingCount = 0;
        for (DriverResponse driver : drivers) {
            Double rating = safeGetDriverRating(token, driver.id());
            if (rating != null) {
                totalRatings += rating;
                ratingCount++;
            }
        }
        double avgDriverRating = ratingCount > 0 ? totalRatings / ratingCount : 4.5; // fallback avg

        // 3. Average Fuel Efficiency
        double totalDistance = trips.stream()
                .map(TripResponse::distanceKm)
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
        double totalLiters = fuelLogs.stream()
                .map(FuelLogResponse::quantityLiters)
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
        double avgEfficiency = totalLiters > 0 ? totalDistance / totalLiters : 8.5; // default fallback km/L

        // 4. Cost Per KM
        BigDecimal totalFuelCost = fuelLogs.stream()
                .map(FuelLogResponse::totalCost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalMaintenanceCost = maintenanceLogs.stream()
                .map(MaintenanceResponse::cost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = expenses.stream()
                .map(ExpenseResponse::amount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCost = totalFuelCost.add(totalMaintenanceCost).add(totalExpenses);
        double costPerKm = totalDistance > 0 ? totalCost.doubleValue() / totalDistance : 0.0;

        // 5. Safety Score (derived from average rating or static fallback)
        double safetyScore = avgDriverRating * 20.0; // scale from 5 stars to 100

        return new KPIsResponse(
                fleetUtilization,
                avgDriverRating,
                avgEfficiency,
                costPerKm,
                safetyScore
        );
    }
}
