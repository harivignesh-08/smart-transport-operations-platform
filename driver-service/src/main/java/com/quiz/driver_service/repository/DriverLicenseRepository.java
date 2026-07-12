package com.quiz.driver_service.repository;

import com.quiz.driver_service.entity.DriverLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverLicenseRepository extends JpaRepository<DriverLicense, Long> {

    List<DriverLicense> findByDriverId(Long driverId);

    Optional<DriverLicense> findByLicenseNumber(String licenseNumber);

    boolean existsByLicenseNumber(String licenseNumber);
}
