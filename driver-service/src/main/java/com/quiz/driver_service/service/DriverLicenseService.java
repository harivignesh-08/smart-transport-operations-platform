package com.quiz.driver_service.service;

import com.quiz.driver_service.dto.LicenseRequest;
import com.quiz.driver_service.dto.LicenseResponse;
import com.quiz.driver_service.entity.Driver;
import com.quiz.driver_service.entity.DriverLicense;
import com.quiz.driver_service.repository.DriverLicenseRepository;
import com.quiz.driver_service.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DriverLicenseService {

    private final DriverLicenseRepository licenseRepository;
    private final DriverRepository driverRepository;

    public DriverLicenseService(DriverLicenseRepository licenseRepository, DriverRepository driverRepository) {
        this.licenseRepository = licenseRepository;
        this.driverRepository = driverRepository;
    }

    public LicenseResponse addLicense(Long driverId, LicenseRequest request) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + driverId));

        if (licenseRepository.existsByLicenseNumber(request.licenseNumber())) {
            throw new IllegalArgumentException("License number " + request.licenseNumber() + " already exists");
        }

        if (request.expiryDate().isBefore(request.issueDate())) {
            throw new IllegalArgumentException("Expiry date must be after issue date");
        }

        DriverLicense license = new DriverLicense(
                request.licenseNumber(),
                request.licenseClass(),
                request.issueDate(),
                request.expiryDate(),
                request.fileUrl(),
                driver
        );

        DriverLicense saved = licenseRepository.save(license);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<LicenseResponse> getLicensesByDriver(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new IllegalArgumentException("Driver not found with id: " + driverId);
        }
        return licenseRepository.findByDriverId(driverId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deleteLicense(Long licenseId) {
        if (!licenseRepository.existsById(licenseId)) {
            throw new IllegalArgumentException("License not found with id: " + licenseId);
        }
        licenseRepository.deleteById(licenseId);
    }

    public LicenseResponse mapToResponse(DriverLicense license) {
        return new LicenseResponse(
                license.getId(),
                license.getLicenseNumber(),
                license.getLicenseClass(),
                license.getIssueDate(),
                license.getExpiryDate(),
                license.getFileUrl(),
                license.getDriver().getId(),
                license.getCreatedAt(),
                license.getUpdatedAt()
        );
    }
}
