package com.quiz.fuel_expense_service.service;

import com.quiz.fuel_expense_service.client.TripClient;
import com.quiz.fuel_expense_service.client.VehicleClient;
import com.quiz.fuel_expense_service.dto.MileageRequest;
import com.quiz.fuel_expense_service.dto.MileageResponse;
import com.quiz.fuel_expense_service.entity.MileageRecord;
import com.quiz.fuel_expense_service.exception.ResourceNotFoundException;
import com.quiz.fuel_expense_service.repository.MileageRecordRepository;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MileageRecordService {

    private final MileageRecordRepository mileageRecordRepository;
    private final VehicleClient vehicleClient;
    private final TripClient tripClient;

    public MileageRecordService(MileageRecordRepository mileageRecordRepository,
                                VehicleClient vehicleClient,
                                TripClient tripClient) {
        this.mileageRecordRepository = mileageRecordRepository;
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

    public MileageResponse createMileageRecord(MileageRequest request) {
        validateVehicleAndTrip(request.vehicleId(), request.tripId());

        if (request.endOdometer().compareTo(request.startOdometer()) < 0) {
            throw new IllegalArgumentException("End odometer must be greater than or equal to start odometer");
        }

        BigDecimal distanceKm = request.endOdometer().subtract(request.startOdometer());

        MileageRecord record = new MileageRecord();
        record.setVehicleId(request.vehicleId());
        record.setDriverId(request.driverId());
        record.setTripId(request.tripId());
        record.setStartOdometer(request.startOdometer());
        record.setEndOdometer(request.endOdometer());
        record.setDistanceKm(distanceKm);
        record.setRecordedAt(request.recordedAt());

        MileageRecord saved = mileageRecordRepository.save(record);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public MileageResponse getMileageRecordById(Long id) {
        MileageRecord record = mileageRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mileage record not found with id: " + id));
        return mapToResponse(record);
    }

    @Transactional(readOnly = true)
    public List<MileageResponse> getMileageRecordsByVehicleId(Long vehicleId) {
        return mileageRecordRepository.findByVehicleId(vehicleId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MileageResponse> getMileageRecordsByTripId(Long tripId) {
        return mileageRecordRepository.findByTripId(tripId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MileageResponse> getAllMileageRecords() {
        return mileageRecordRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MileageResponse mapToResponse(MileageRecord record) {
        return new MileageResponse(
                record.getId(),
                record.getVehicleId(),
                record.getDriverId(),
                record.getTripId(),
                record.getStartOdometer(),
                record.getEndOdometer(),
                record.getDistanceKm(),
                record.getRecordedAt(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}
