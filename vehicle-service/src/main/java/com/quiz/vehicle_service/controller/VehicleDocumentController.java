package com.quiz.vehicle_service.controller;

import com.quiz.vehicle_service.dto.DocumentRequest;
import com.quiz.vehicle_service.dto.DocumentResponse;
import com.quiz.vehicle_service.service.VehicleDocumentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleDocumentController {

    private final VehicleDocumentService documentService;

    public VehicleDocumentController(VehicleDocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/{vehicleId}/documents")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DocumentResponse> addDocument(
            @PathVariable Long vehicleId,
            @Valid @RequestBody DocumentRequest request) {
        DocumentResponse response = documentService.addDocument(vehicleId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{vehicleId}/documents")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByVehicle(@PathVariable Long vehicleId) {
        List<DocumentResponse> response = documentService.getDocumentsByVehicle(vehicleId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/documents/{documentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}
