package com.quiz.driver_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "driver_performances")
public class DriverPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false, unique = true)
    @JsonIgnore
    private Driver driver;

    @NotNull
    @Column(nullable = false)
    private Integer totalTrips = 0;

    @NotNull
    @Column(nullable = false)
    private Integer completedTrips = 0;

    @NotNull
    @Column(nullable = false)
    private Integer cancelledTrips = 0;

    @Column(nullable = false)
    private Double averageRating = 0.0;

    @Column(nullable = false)
    private Double onTimePercentage = 100.0;

    @Column(nullable = false)
    private Double totalDistanceKm = 0.0;

    private LocalDateTime lastUpdated;

    public DriverPerformance() {}

    public DriverPerformance(Driver driver) {
        this.driver = driver;
        this.lastUpdated = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    protected void onSave() {
        lastUpdated = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Integer getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(Integer totalTrips) {
        this.totalTrips = totalTrips;
    }

    public Integer getCompletedTrips() {
        return completedTrips;
    }

    public void setCompletedTrips(Integer completedTrips) {
        this.completedTrips = completedTrips;
    }

    public Integer getCancelledTrips() {
        return cancelledTrips;
    }

    public void setCancelledTrips(Integer cancelledTrips) {
        this.cancelledTrips = cancelledTrips;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Double getOnTimePercentage() {
        return onTimePercentage;
    }

    public void setOnTimePercentage(Double onTimePercentage) {
        this.onTimePercentage = onTimePercentage;
    }

    public Double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public void setTotalDistanceKm(Double totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
