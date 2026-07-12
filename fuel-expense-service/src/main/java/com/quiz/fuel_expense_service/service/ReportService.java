package com.quiz.fuel_expense_service.service;

import com.quiz.fuel_expense_service.client.VehicleClient;
import com.quiz.fuel_expense_service.dto.ExpenseSummaryReport;
import com.quiz.fuel_expense_service.dto.FuelSummaryReport;
import com.quiz.fuel_expense_service.dto.MileageSummaryReport;
import com.quiz.fuel_expense_service.entity.Expense;
import com.quiz.fuel_expense_service.entity.ExpenseCategory;
import com.quiz.fuel_expense_service.entity.FuelLog;
import com.quiz.fuel_expense_service.entity.MileageRecord;
import com.quiz.fuel_expense_service.exception.ResourceNotFoundException;
import com.quiz.fuel_expense_service.repository.ExpenseRepository;
import com.quiz.fuel_expense_service.repository.FuelLogRepository;
import com.quiz.fuel_expense_service.repository.MileageRecordRepository;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private final FuelLogRepository fuelLogRepository;
    private final ExpenseRepository expenseRepository;
    private final MileageRecordRepository mileageRecordRepository;
    private final VehicleClient vehicleClient;

    public ReportService(FuelLogRepository fuelLogRepository,
                         ExpenseRepository expenseRepository,
                         MileageRecordRepository mileageRecordRepository,
                         VehicleClient vehicleClient) {
        this.fuelLogRepository = fuelLogRepository;
        this.expenseRepository = expenseRepository;
        this.mileageRecordRepository = mileageRecordRepository;
        this.vehicleClient = vehicleClient;
    }

    private String getAuthToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getHeader("Authorization");
        }
        return null;
    }

    private void validateVehicleExists(Long vehicleId) {
        String token = getAuthToken();
        if (token == null) {
            throw new IllegalArgumentException("No authentication token found in request context");
        }
        try {
            vehicleClient.getVehicleById(token, vehicleId);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + vehicleId);
        } catch (FeignException e) {
            throw new IllegalArgumentException("Failed to validate vehicle: " + e.getMessage());
        }
    }

    public FuelSummaryReport generateFuelReport(Long vehicleId, LocalDateTime from, LocalDateTime to) {
        validateVehicleExists(vehicleId);

        List<FuelLog> logs = fuelLogRepository.findByVehicleIdAndFilledAtBetween(vehicleId, from, to);
        long totalFuelLogs = logs.size();

        BigDecimal totalLiters = logs.stream()
                .map(FuelLog::getQuantityLiters)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalFuelCost = logs.stream()
                .map(FuelLog::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageCostPerLiter = BigDecimal.ZERO;
        if (totalLiters.compareTo(BigDecimal.ZERO) > 0) {
            averageCostPerLiter = totalFuelCost.divide(totalLiters, 4, RoundingMode.HALF_UP);
        }

        return new FuelSummaryReport(
                vehicleId,
                from,
                to,
                totalFuelLogs,
                totalLiters,
                totalFuelCost,
                averageCostPerLiter
        );
    }

    public ExpenseSummaryReport generateExpenseReport(Long vehicleId, LocalDateTime from, LocalDateTime to) {
        validateVehicleExists(vehicleId);

        List<Expense> expenses = expenseRepository.findByVehicleIdAndExpenseDateBetween(vehicleId, from, to);
        long totalExpenses = expenses.size();

        BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Pre-populate with all categories set to zero
        Map<ExpenseCategory, BigDecimal> amountByCategory = new EnumMap<>(ExpenseCategory.class);
        for (ExpenseCategory cat : ExpenseCategory.values()) {
            amountByCategory.put(cat, BigDecimal.ZERO);
        }

        // Sum amounts by category
        expenses.forEach(expense -> {
            BigDecimal current = amountByCategory.getOrDefault(expense.getCategory(), BigDecimal.ZERO);
            amountByCategory.put(expense.getCategory(), current.add(expense.getAmount()));
        });

        return new ExpenseSummaryReport(
                vehicleId,
                from,
                to,
                totalExpenses,
                totalAmount,
                amountByCategory
        );
    }

    public MileageSummaryReport generateMileageReport(Long vehicleId, LocalDateTime from, LocalDateTime to) {
        validateVehicleExists(vehicleId);

        List<MileageRecord> records = mileageRecordRepository.findByVehicleIdAndRecordedAtBetween(vehicleId, from, to);
        long totalRecords = records.size();

        BigDecimal totalDistanceKm = records.stream()
                .map(MileageRecord::getDistanceKm)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageDistanceKm = BigDecimal.ZERO;
        if (totalRecords > 0) {
            averageDistanceKm = totalDistanceKm.divide(BigDecimal.valueOf(totalRecords), 2, RoundingMode.HALF_UP);
        }

        return new MileageSummaryReport(
                vehicleId,
                from,
                to,
                totalRecords,
                totalDistanceKm,
                averageDistanceKm
        );
    }
}
