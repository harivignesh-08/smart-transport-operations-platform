package com.quiz.maintenance_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "predictive_maintenances")
public class PredictiveMaintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long vehicleId;

    @NotNull
    @Column(nullable = false)
    private Boolean maintenanceRequired;

    @NotNull
    @Column(nullable = false)
    private Double maintenanceScore;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "predictive_maintenance_suggestions", joinColumns = @JoinColumn(name = "prediction_id"))
    @Column(name = "suggestion", nullable = false)
    private List<String> suggestedMaintenance = new ArrayList<>();

    @NotNull
    @Column(nullable = false)
    private LocalDateTime predictionDate;

    public PredictiveMaintenance() {}

    public PredictiveMaintenance(Long vehicleId, Boolean maintenanceRequired, Double maintenanceScore, List<String> suggestedMaintenance) {
        this.vehicleId = vehicleId;
        this.maintenanceRequired = maintenanceRequired;
        this.maintenanceScore = maintenanceScore;
        this.suggestedMaintenance = suggestedMaintenance;
        this.predictionDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Boolean getMaintenanceRequired() {
        return maintenanceRequired;
    }

    public void setMaintenanceRequired(Boolean maintenanceRequired) {
        this.maintenanceRequired = maintenanceRequired;
    }

    public Double getMaintenanceScore() {
        return maintenanceScore;
    }

    public void setMaintenanceScore(Double maintenanceScore) {
        this.maintenanceScore = maintenanceScore;
    }

    public List<String> getSuggestedMaintenance() {
        return suggestedMaintenance;
    }

    public void setSuggestedMaintenance(List<String> suggestedMaintenance) {
        this.suggestedMaintenance = suggestedMaintenance;
    }

    public LocalDateTime getPredictionDate() {
        return predictionDate;
    }

    public void setPredictionDate(LocalDateTime predictionDate) {
        this.predictionDate = predictionDate;
    }
}
