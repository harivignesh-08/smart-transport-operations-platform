package com.quiz.vehicle_service.service;

import com.quiz.vehicle_service.dto.DocumentRequest;
import com.quiz.vehicle_service.dto.DocumentResponse;
import com.quiz.vehicle_service.entity.Vehicle;
import com.quiz.vehicle_service.entity.VehicleDocument;
import com.quiz.vehicle_service.repository.VehicleDocumentRepository;
import com.quiz.vehicle_service.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleDocumentService {

    private final VehicleDocumentRepository documentRepository;
    private final VehicleRepository vehicleRepository;

    public VehicleDocumentService(VehicleDocumentRepository documentRepository, VehicleRepository vehicleRepository) {
        this.documentRepository = documentRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public DocumentResponse addDocument(Long vehicleId, DocumentRequest request) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + vehicleId));

        VehicleDocument document = new VehicleDocument(
                request.documentType(),
                request.documentNumber(),
                request.expiryDate(),
                request.fileUrl(),
                vehicle
        );

        VehicleDocument saved = documentRepository.save(document);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<DocumentResponse> getDocumentsByVehicle(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new IllegalArgumentException("Vehicle not found with id: " + vehicleId);
        }
        return documentRepository.findByVehicleId(vehicleId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deleteDocument(Long documentId) {
        if (!documentRepository.existsById(documentId)) {
            throw new IllegalArgumentException("Document not found with id: " + documentId);
        }
        documentRepository.deleteById(documentId);
    }

    public DocumentResponse mapToResponse(VehicleDocument document) {
        return new DocumentResponse(
                document.getId(),
                document.getDocumentType(),
                document.getDocumentNumber(),
                document.getExpiryDate(),
                document.getFileUrl(),
                document.getVehicle().getId(),
                document.getCreatedAt(),
                document.getUpdatedAt()
        );
    }
}
