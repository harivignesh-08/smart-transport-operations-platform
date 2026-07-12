package com.quiz.vehicle_service.service;

import com.quiz.vehicle_service.dto.AvailabilityUpdateRequest;
import com.quiz.vehicle_service.dto.StatusUpdateRequest;
import com.quiz.vehicle_service.dto.VehicleRequest;
import com.quiz.vehicle_service.dto.VehicleResponse;
import com.quiz.vehicle_service.entity.AvailabilityStatus;
import com.quiz.vehicle_service.entity.Vehicle;
import com.quiz.vehicle_service.entity.VehicleStatus;
import com.quiz.vehicle_service.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public VehicleResponse createVehicle(VehicleRequest request) {
        if (vehicleRepository.existsByLicensePlate(request.licensePlate())) {
            throw new IllegalArgumentException("Vehicle with license plate " + request.licensePlate() + " already exists!");
        }

        Vehicle vehicle = new Vehicle(
                request.licensePlate(),
                request.make(),
                request.model(),
                request.year()
        );

        if (request.status() != null) {
            vehicle.setStatus(VehicleStatus.valueOf(request.status().toUpperCase()));
        }
        if (request.availabilityStatus() != null) {
            vehicle.setAvailabilityStatus(AvailabilityStatus.valueOf(request.availabilityStatus().toUpperCase()));
        }
        vehicle.setDriverId(request.driverId());

        Vehicle saved = vehicleRepository.save(vehicle);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public VehicleResponse getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + id));
        return mapToResponse(vehicle);
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public VehicleResponse updateVehicle(Long id, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + id));

        if (!vehicle.getLicensePlate().equals(request.licensePlate()) &&
                vehicleRepository.existsByLicensePlate(request.licensePlate())) {
            throw new IllegalArgumentException("Vehicle with license plate " + request.licensePlate() + " already exists!");
        }

        vehicle.setLicensePlate(request.licensePlate());
        vehicle.setMake(request.make());
        vehicle.setModel(request.model());
        vehicle.setYear(request.year());

        if (request.status() != null) {
            vehicle.setStatus(VehicleStatus.valueOf(request.status().toUpperCase()));
        }
        if (request.availabilityStatus() != null) {
            vehicle.setAvailabilityStatus(AvailabilityStatus.valueOf(request.availabilityStatus().toUpperCase()));
        }
        vehicle.setDriverId(request.driverId());

        Vehicle updated = vehicleRepository.save(vehicle);
        return mapToResponse(updated);
    }

    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new IllegalArgumentException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    public VehicleResponse updateVehicleStatus(Long id, StatusUpdateRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + id));

        try {
            VehicleStatus newStatus = VehicleStatus.valueOf(request.status().toUpperCase());
            vehicle.setStatus(newStatus);
            // If the vehicle is undergoing maintenance or is inactive, it shouldn't be available
            if (newStatus == VehicleStatus.MAINTENANCE || newStatus == VehicleStatus.INACTIVE) {
                vehicle.setAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + request.status());
        }

        Vehicle updated = vehicleRepository.save(vehicle);
        return mapToResponse(updated);
    }

    public VehicleResponse updateVehicleAvailability(Long id, AvailabilityUpdateRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + id));

        try {
            AvailabilityStatus newAvailability = AvailabilityStatus.valueOf(request.availabilityStatus().toUpperCase());
            vehicle.setAvailabilityStatus(newAvailability);
            vehicle.setDriverId(request.driverId());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid availability status value: " + request.availabilityStatus());
        }

        Vehicle updated = vehicleRepository.save(vehicle);
        return mapToResponse(updated);
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> getAvailableVehicles() {
        return vehicleRepository.findByAvailabilityStatus(AvailabilityStatus.AVAILABLE).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> getVehiclesByDriver(Long driverId) {
        return vehicleRepository.findByDriverId(driverId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public VehicleResponse mapToResponse(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getLicensePlate(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getStatus(),
                vehicle.getAvailabilityStatus(),
                vehicle.getDriverId(),
                vehicle.getCreatedAt(),
                vehicle.getUpdatedAt()
        );
    }
}
