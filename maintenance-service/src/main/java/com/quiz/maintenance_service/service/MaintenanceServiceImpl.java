package com.quiz.maintenance_service.service;

import com.quiz.maintenance_service.client.AIClient;
import com.quiz.maintenance_service.client.NotificationClient;
import com.quiz.maintenance_service.client.VehicleClient;
import com.quiz.maintenance_service.dto.*;
import com.quiz.maintenance_service.entity.*;
import com.quiz.maintenance_service.repository.MaintenanceScheduleRepository;
import com.quiz.maintenance_service.repository.PredictiveMaintenanceRepository;
import com.quiz.maintenance_service.repository.RepairRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceScheduleRepository scheduleRepository;
    private final RepairRepository repairRepository;
    private final PredictiveMaintenanceRepository predictiveRepository;

    private final VehicleClient vehicleClient;
    private final NotificationClient notificationClient;
    private final AIClient aiClient;

    public MaintenanceServiceImpl(
            MaintenanceScheduleRepository scheduleRepository,
            RepairRepository repairRepository,
            PredictiveMaintenanceRepository predictiveRepository,
            VehicleClient vehicleClient,
            NotificationClient notificationClient,
            AIClient aiClient) {
        this.scheduleRepository = scheduleRepository;
        this.repairRepository = repairRepository;
        this.predictiveRepository = predictiveRepository;
        this.vehicleClient = vehicleClient;
        this.notificationClient = notificationClient;
        this.aiClient = aiClient;
    }

    // --- SCHEDULES ---

    @Override
    public MaintenanceSchedule createSchedule(MaintenanceScheduleRequest request) {
        // Verify vehicle exists
        verifyVehicleExists(request.vehicleId());

        MaintenanceSchedule schedule = new MaintenanceSchedule(
                request.vehicleId(),
                request.serviceType(),
                request.scheduledDate(),
                request.notes()
        );
        return scheduleRepository.save(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceSchedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceSchedule getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maintenance schedule not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceSchedule> getSchedulesByVehicleId(Long vehicleId) {
        return scheduleRepository.findByVehicleId(vehicleId);
    }

    @Override
    public MaintenanceSchedule updateSchedule(Long id, MaintenanceScheduleRequest request) {
        MaintenanceSchedule schedule = getScheduleById(id);
        verifyVehicleExists(request.vehicleId());

        schedule.setVehicleId(request.vehicleId());
        schedule.setServiceType(request.serviceType());
        schedule.setScheduledDate(request.scheduledDate());
        schedule.setNotes(request.notes());

        return scheduleRepository.save(schedule);
    }

    @Override
    public MaintenanceSchedule updateScheduleStatus(Long id, String statusStr) {
        MaintenanceSchedule schedule = getScheduleById(id);
        ScheduleStatus status;
        try {
            status = ScheduleStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid schedule status: " + statusStr);
        }

        schedule.setStatus(status);
        schedule = scheduleRepository.save(schedule);

        // Integrate with Vehicle Service to update vehicle state based on schedule status
        try {
            if (status == ScheduleStatus.IN_PROGRESS) {
                vehicleClient.updateVehicleStatus(schedule.getVehicleId(), new StatusUpdateRequest("MAINTENANCE"));
                vehicleClient.updateVehicleAvailability(schedule.getVehicleId(), new AvailabilityUpdateRequest("UNAVAILABLE", null));
            } else if (status == ScheduleStatus.COMPLETED || status == ScheduleStatus.CANCELLED) {
                vehicleClient.updateVehicleStatus(schedule.getVehicleId(), new StatusUpdateRequest("ACTIVE"));
                vehicleClient.updateVehicleAvailability(schedule.getVehicleId(), new AvailabilityUpdateRequest("AVAILABLE", null));
            }
        } catch (Exception e) {
            // Log warning but don't fail transaction if vehicle service state update fails
            System.err.println("Warning: Failed to update vehicle status for vehicle ID " 
                    + schedule.getVehicleId() + ": " + e.getMessage());
        }

        return schedule;
    }

    @Override
    public void deleteSchedule(Long id) {
        MaintenanceSchedule schedule = getScheduleById(id);
        scheduleRepository.delete(schedule);
    }

    // --- REPAIRS ---

    @Override
    public Repair logRepair(RepairRequest request) {
        verifyVehicleExists(request.vehicleId());

        Repair repair = new Repair(
                request.vehicleId(),
                request.description(),
                request.cost(),
                request.startDate(),
                request.endDate()
        );

        if (request.status() != null) {
            try {
                repair.setStatus(RepairStatus.valueOf(request.status().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid repair status: " + request.status());
            }
        }

        repair = repairRepository.save(repair);

        // If repair is currently in progress, update vehicle state
        updateVehicleStateForRepair(repair);

        return repair;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Repair> getAllRepairs() {
        return repairRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Repair getRepairById(Long id) {
        return repairRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Repair log not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Repair> getRepairsByVehicleId(Long vehicleId) {
        return repairRepository.findByVehicleId(vehicleId);
    }

    @Override
    public Repair updateRepair(Long id, RepairRequest request) {
        Repair repair = getRepairById(id);
        verifyVehicleExists(request.vehicleId());

        repair.setVehicleId(request.vehicleId());
        repair.setDescription(request.description());
        repair.setCost(request.cost());
        repair.setStartDate(request.startDate());
        repair.setEndDate(request.endDate());

        if (request.status() != null) {
            try {
                repair.setStatus(RepairStatus.valueOf(request.status().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid repair status: " + request.status());
            }
        }

        repair = repairRepository.save(repair);
        updateVehicleStateForRepair(repair);

        return repair;
    }

    @Override
    public void deleteRepair(Long id) {
        Repair repair = getRepairById(id);
        repairRepository.delete(repair);
    }

    // --- PREDICTIVE MAINTENANCE ---

    @Override
    public PredictiveMaintenance performPredictiveMaintenance(Long vehicleId, PredictiveMaintenanceRequest request) {
        // 1. Verify vehicle exists locally / via vehicle service
        verifyVehicleExists(vehicleId);

        // 2. Call AI Service via OpenFeign
        MaintenancePredictionRequest aiRequest = new MaintenancePredictionRequest(
                String.valueOf(vehicleId),
                request.totalDistance(),
                request.engineHours(),
                request.fuelConsumption(),
                request.lastServiceDate().toString()
        );

        MaintenancePredictionResponse aiResponse;
        try {
            aiResponse = aiClient.predictMaintenance(aiRequest);
        } catch (Exception e) {
            throw new RuntimeException("AI predictive maintenance model execution failed: " + e.getMessage(), e);
        }

        // 3. Log predictive maintenance result
        PredictiveMaintenance prediction = new PredictiveMaintenance(
                vehicleId,
                aiResponse.maintenanceRequired(),
                aiResponse.maintenanceScore(),
                aiResponse.suggestedMaintenance()
        );
        prediction = predictiveRepository.save(prediction);

        // 4. Act if maintenance is recommended immediately
        if (aiResponse.maintenanceRequired()) {
            // Update Vehicle Status
            try {
                vehicleClient.updateVehicleStatus(vehicleId, new StatusUpdateRequest("MAINTENANCE"));
                vehicleClient.updateVehicleAvailability(vehicleId, new AvailabilityUpdateRequest("UNAVAILABLE", null));
            } catch (Exception e) {
                System.err.println("Warning: Failed to set vehicle status to MAINTENANCE: " + e.getMessage());
            }

            // Dispatch notification alert
            try {
                String message = String.format("ML prediction alert: Vehicle %d has high failure risk (score: %.1f%%). Suggestions: %s",
                        vehicleId, aiResponse.maintenanceScore(), String.join(", ", aiResponse.suggestedMaintenance()));
                NotificationRequest notificationRequest = new NotificationRequest(
                        0L, // dummy tripId
                        "ADMIN",
                        "admin",
                        "PREDICTIVE_MAINTENANCE_ALERT",
                        message
                );
                notificationClient.sendNotification(notificationRequest);
            } catch (Exception e) {
                System.err.println("Warning: Failed to send predictive maintenance alert notification: " + e.getMessage());
            }
        }

        return prediction;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PredictiveMaintenance> getPredictionsByVehicleId(Long vehicleId) {
        return predictiveRepository.findByVehicleIdOrderByPredictionDateDesc(vehicleId);
    }

    // --- HELPER METHODS ---

    private void verifyVehicleExists(Long vehicleId) {
        try {
            VehicleResponse vehicle = vehicleClient.getVehicleById(vehicleId);
            if (vehicle == null) {
                throw new IllegalArgumentException("Vehicle does not exist with ID: " + vehicleId);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to verify vehicle existence from vehicle service: " + e.getMessage());
        }
    }

    private void updateVehicleStateForRepair(Repair repair) {
        try {
            if (repair.getStatus() == RepairStatus.IN_PROGRESS) {
                vehicleClient.updateVehicleStatus(repair.getVehicleId(), new StatusUpdateRequest("MAINTENANCE"));
                vehicleClient.updateVehicleAvailability(repair.getVehicleId(), new AvailabilityUpdateRequest("UNAVAILABLE", null));
            } else if (repair.getStatus() == RepairStatus.COMPLETED) {
                vehicleClient.updateVehicleStatus(repair.getVehicleId(), new StatusUpdateRequest("ACTIVE"));
                vehicleClient.updateVehicleAvailability(repair.getVehicleId(), new AvailabilityUpdateRequest("AVAILABLE", null));
            }
        } catch (Exception e) {
            System.err.println("Warning: Failed to sync vehicle status with repair status: " + e.getMessage());
        }
    }
}
