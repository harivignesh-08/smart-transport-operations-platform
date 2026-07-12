package com.quiz.fuel_expense_service.service;

import com.quiz.fuel_expense_service.client.TripClient;
import com.quiz.fuel_expense_service.client.VehicleClient;
import com.quiz.fuel_expense_service.dto.ExpenseRequest;
import com.quiz.fuel_expense_service.dto.ExpenseResponse;
import com.quiz.fuel_expense_service.entity.Expense;
import com.quiz.fuel_expense_service.entity.ExpenseCategory;
import com.quiz.fuel_expense_service.exception.ResourceNotFoundException;
import com.quiz.fuel_expense_service.repository.ExpenseRepository;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final VehicleClient vehicleClient;
    private final TripClient tripClient;

    public ExpenseService(ExpenseRepository expenseRepository,
                          VehicleClient vehicleClient,
                          TripClient tripClient) {
        this.expenseRepository = expenseRepository;
        this.vehicleClient = vehicleClient;
        this.tripClient = tripClient;
    }

    private String getAuthToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getHeader("Authorization");
        }
        return null;
    }

    private void validateVehicleAndTrip(Long vehicleId, Long tripId) {
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

        if (tripId != null) {
            try {
                tripClient.getTripById(token, tripId);
            } catch (FeignException.NotFound e) {
                throw new ResourceNotFoundException("Trip not found with id: " + tripId);
            } catch (FeignException e) {
                throw new IllegalArgumentException("Failed to validate trip: " + e.getMessage());
            }
        }
    }

    public ExpenseResponse createExpense(ExpenseRequest request) {
        validateVehicleAndTrip(request.vehicleId(), request.tripId());

        Expense expense = new Expense();
        expense.setVehicleId(request.vehicleId());
        expense.setDriverId(request.driverId());
        expense.setTripId(request.tripId());
        
        try {
            expense.setCategory(ExpenseCategory.valueOf(request.category().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid expense category: " + request.category());
        }

        expense.setAmount(request.amount());
        expense.setDescription(request.description());
        expense.setReceiptUrl(request.receiptUrl());
        expense.setExpenseDate(request.expenseDate());

        Expense saved = expenseRepository.save(expense);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        return mapToResponse(expense);
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByVehicleId(Long vehicleId) {
        return expenseRepository.findByVehicleId(vehicleId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByTripId(Long tripId) {
        return expenseRepository.findByTripId(tripId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByVehicleIdAndCategory(Long vehicleId, String category) {
        try {
            ExpenseCategory expenseCategory = ExpenseCategory.valueOf(category.toUpperCase());
            return expenseRepository.findByVehicleIdAndCategory(vehicleId, expenseCategory).stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid expense category: " + category);
        }
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ExpenseResponse mapToResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getVehicleId(),
                expense.getDriverId(),
                expense.getTripId(),
                expense.getCategory(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getReceiptUrl(),
                expense.getExpenseDate(),
                expense.getCreatedAt(),
                expense.getUpdatedAt()
        );
    }
}
