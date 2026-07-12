package com.quiz.fuel_expense_service.service;

import com.quiz.fuel_expense_service.client.TripClient;
import com.quiz.fuel_expense_service.client.VehicleClient;
import com.quiz.fuel_expense_service.dto.FuelLogRequest;
import com.quiz.fuel_expense_service.dto.FuelLogResponse;
import com.quiz.fuel_expense_service.entity.FuelLog;
import com.quiz.fuel_expense_service.entity.FuelType;
import com.quiz.fuel_expense_service.exception.ResourceNotFoundException;
import com.quiz.fuel_expense_service.repository.FuelLogRepository;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FuelLogService {

    private final FuelLogRepository fuelLogRepository;
    private final VehicleClient vehicleClient;
    private final TripClient tripClient;

    public FuelLogService(FuelLogRepository fuelLogRepository,
                          VehicleClient vehicleClient,
                          TripClient tripClient) {
        this.fuelLogRepository = fuelLogRepository;
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

    public FuelLogResponse createFuelLog(FuelLogRequest request) {
        validateVehicleAndTrip(request.vehicleId(), request.tripId());

        FuelLog fuelLog = new FuelLog();
        fuelLog.setVehicleId(request.vehicleId());
        fuelLog.setDriverId(request.driverId());
        fuelLog.setTripId(request.tripId());
        
        try {
            fuelLog.setFuelType(FuelType.valueOf(request.fuelType().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid fuel type: " + request.fuelType());
        }

        fuelLog.setQuantityLiters(request.quantityLiters());
        fuelLog.setCostPerLiter(request.costPerLiter());
        
        // Auto-calculate total cost
        BigDecimal totalCost = request.quantityLiters().multiply(request.costPerLiter());
        fuelLog.setTotalCost(totalCost);
        
        fuelLog.setOdometerReading(request.odometerReading());
        fuelLog.setStationName(request.stationName());
        fuelLog.setFilledAt(request.filledAt());

        FuelLog saved = fuelLogRepository.save(fuelLog);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public FuelLogResponse getFuelLogById(Long id) {
        FuelLog fuelLog = fuelLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fuel log not found with id: " + id));
        return mapToResponse(fuelLog);
    }

    @Transactional(readOnly = true)
    public List<FuelLogResponse> getFuelLogsByVehicleId(Long vehicleId) {
        return fuelLogRepository.findByVehicleId(vehicleId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FuelLogResponse> getFuelLogsByTripId(Long tripId) {
        return fuelLogRepository.findByTripId(tripId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FuelLogResponse> getAllFuelLogs() {
        return fuelLogRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public FuelLogResponse mapToResponse(FuelLog fuelLog) {
        return new FuelLogResponse(
                fuelLog.getId(),
                fuelLog.getVehicleId(),
                fuelLog.getDriverId(),
                fuelLog.getTripId(),
                fuelLog.getFuelType(),
                fuelLog.getQuantityLiters(),
                fuelLog.getCostPerLiter(),
                fuelLog.getTotalCost(),
                fuelLog.getOdometerReading(),
                fuelLog.getStationName(),
                fuelLog.getFilledAt(),
                fuelLog.getCreatedAt(),
                fuelLog.getUpdatedAt()
        );
    }
}
