package com.quiz.driver_service.controller;

import com.quiz.driver_service.dto.LicenseRequest;
import com.quiz.driver_service.dto.LicenseResponse;
import com.quiz.driver_service.service.DriverLicenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverLicenseController {

    private final DriverLicenseService licenseService;

    public DriverLicenseController(DriverLicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @PostMapping("/{driverId}/licenses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LicenseResponse> addLicense(
            @PathVariable Long driverId,
            @Valid @RequestBody LicenseRequest request) {
        LicenseResponse response = licenseService.addLicense(driverId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{driverId}/licenses")
    public ResponseEntity<List<LicenseResponse>> getLicensesByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(licenseService.getLicensesByDriver(driverId));
    }

    @DeleteMapping("/licenses/{licenseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLicense(@PathVariable Long licenseId) {
        licenseService.deleteLicense(licenseId);
        return ResponseEntity.noContent().build();
    }
}
