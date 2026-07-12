package com.quiz.driver_service.service;

import com.quiz.driver_service.dto.AvailabilityUpdateRequest;
import com.quiz.driver_service.dto.DriverRequest;
import com.quiz.driver_service.dto.DriverResponse;
import com.quiz.driver_service.dto.StatusUpdateRequest;
import com.quiz.driver_service.entity.AvailabilityStatus;
import com.quiz.driver_service.entity.Driver;
import com.quiz.driver_service.entity.DriverPerformance;
import com.quiz.driver_service.entity.DriverStatus;
import com.quiz.driver_service.repository.DriverPerformanceRepository;
import com.quiz.driver_service.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverPerformanceRepository performanceRepository;

    public DriverService(DriverRepository driverRepository,
                         DriverPerformanceRepository performanceRepository) {
        this.driverRepository = driverRepository;
        this.performanceRepository = performanceRepository;
    }

    public DriverResponse createDriver(DriverRequest request) {
        if (driverRepository.existsByUserId(request.userId())) {
            throw new IllegalArgumentException("Driver profile already exists for user id: " + request.userId());
        }

        Driver driver = new Driver(
                request.userId(),
                request.firstName(),
                request.lastName(),
                request.phone(),
                request.email()
        );

        if (request.status() != null) {
            driver.setStatus(DriverStatus.valueOf(request.status().toUpperCase()));
        }
        if (request.availabilityStatus() != null) {
            driver.setAvailabilityStatus(AvailabilityStatus.valueOf(request.availabilityStatus().toUpperCase()));
        }
        driver.setVehicleId(request.vehicleId());

        Driver saved = driverRepository.save(driver);
        performanceRepository.save(new DriverPerformance(saved));

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public DriverResponse getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + id));
        return mapToResponse(driver);
    }

    @Transactional(readOnly = true)
    public DriverResponse getDriverByUserId(Long userId) {
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found for user id: " + userId));
        return mapToResponse(driver);
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DriverResponse updateDriver(Long id, DriverRequest request) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + id));

        if (!driver.getUserId().equals(request.userId()) &&
                driverRepository.existsByUserId(request.userId())) {
            throw new IllegalArgumentException("Driver profile already exists for user id: " + request.userId());
        }

        driver.setUserId(request.userId());
        driver.setFirstName(request.firstName());
        driver.setLastName(request.lastName());
        driver.setPhone(request.phone());
        driver.setEmail(request.email());

        if (request.status() != null) {
            driver.setStatus(DriverStatus.valueOf(request.status().toUpperCase()));
        }
        if (request.availabilityStatus() != null) {
            driver.setAvailabilityStatus(AvailabilityStatus.valueOf(request.availabilityStatus().toUpperCase()));
        }
        driver.setVehicleId(request.vehicleId());

        Driver updated = driverRepository.save(driver);
        return mapToResponse(updated);
    }

    public void deleteDriver(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new IllegalArgumentException("Driver not found with id: " + id);
        }
        driverRepository.deleteById(id);
    }

    public DriverResponse updateDriverStatus(Long id, StatusUpdateRequest request) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + id));

        try {
            DriverStatus newStatus = DriverStatus.valueOf(request.status().toUpperCase());
            driver.setStatus(newStatus);
            if (newStatus == DriverStatus.INACTIVE || newStatus == DriverStatus.SUSPENDED) {
                driver.setAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);
                driver.setVehicleId(null);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + request.status());
        }

        Driver updated = driverRepository.save(driver);
        return mapToResponse(updated);
    }

    public DriverResponse updateDriverAvailability(Long id, AvailabilityUpdateRequest request) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + id));

        if (driver.getStatus() != DriverStatus.ACTIVE) {
            throw new IllegalArgumentException("Only active drivers can update availability");
        }

        try {
            AvailabilityStatus newAvailability = AvailabilityStatus.valueOf(request.availabilityStatus().toUpperCase());
            driver.setAvailabilityStatus(newAvailability);
            if (newAvailability == AvailabilityStatus.ON_TRIP) {
                driver.setVehicleId(request.vehicleId());
            } else if (newAvailability == AvailabilityStatus.AVAILABLE ||
                    newAvailability == AvailabilityStatus.OFF_DUTY) {
                driver.setVehicleId(null);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid availability status value: " + request.availabilityStatus());
        }

        Driver updated = driverRepository.save(driver);
        return mapToResponse(updated);
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> getAvailableDrivers() {
        return driverRepository.findByAvailabilityStatus(AvailabilityStatus.AVAILABLE).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> getDriversByVehicle(Long vehicleId) {
        return driverRepository.findByVehicleId(vehicleId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DriverResponse mapToResponse(Driver driver) {
        return new DriverResponse(
                driver.getId(),
                driver.getUserId(),
                driver.getFirstName(),
                driver.getLastName(),
                driver.getPhone(),
                driver.getEmail(),
                driver.getStatus(),
                driver.getAvailabilityStatus(),
                driver.getVehicleId(),
                driver.getCreatedAt(),
                driver.getUpdatedAt()
        );
    }
}
