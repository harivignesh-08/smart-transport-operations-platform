package com.quiz.driver_service.repository;

import com.quiz.driver_service.entity.AvailabilityStatus;
import com.quiz.driver_service.entity.Driver;
import com.quiz.driver_service.entity.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByUserId(Long userId);

    List<Driver> findByAvailabilityStatus(AvailabilityStatus availabilityStatus);

    List<Driver> findByStatus(DriverStatus status);

    List<Driver> findByVehicleId(Long vehicleId);

    boolean existsByUserId(Long userId);
}
