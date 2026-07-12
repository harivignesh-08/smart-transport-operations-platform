package com.quiz.vehicle_service.repository;

import com.quiz.vehicle_service.entity.AvailabilityStatus;
import com.quiz.vehicle_service.entity.Vehicle;
import com.quiz.vehicle_service.entity.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    List<Vehicle> findByAvailabilityStatus(AvailabilityStatus availabilityStatus);

    List<Vehicle> findByStatus(VehicleStatus status);

    List<Vehicle> findByDriverId(Long driverId);

    boolean existsByLicensePlate(String licensePlate);
}
