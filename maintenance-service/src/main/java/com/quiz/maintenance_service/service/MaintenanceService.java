package com.quiz.maintenance_service.service;

import com.quiz.maintenance_service.dto.*;
import com.quiz.maintenance_service.entity.MaintenanceSchedule;
import com.quiz.maintenance_service.entity.PredictiveMaintenance;
import com.quiz.maintenance_service.entity.Repair;

import java.util.List;

public interface MaintenanceService {

    // Schedules
    MaintenanceSchedule createSchedule(MaintenanceScheduleRequest request);
    List<MaintenanceSchedule> getAllSchedules();
    MaintenanceSchedule getScheduleById(Long id);
    List<MaintenanceSchedule> getSchedulesByVehicleId(Long vehicleId);
    MaintenanceSchedule updateSchedule(Long id, MaintenanceScheduleRequest request);
    MaintenanceSchedule updateScheduleStatus(Long id, String status);
    void deleteSchedule(Long id);

    // Repairs
    Repair logRepair(RepairRequest request);
    List<Repair> getAllRepairs();
    Repair getRepairById(Long id);
    List<Repair> getRepairsByVehicleId(Long vehicleId);
    Repair updateRepair(Long id, RepairRequest request);
    void deleteRepair(Long id);

    // Predictive Maintenance
    PredictiveMaintenance performPredictiveMaintenance(Long vehicleId, PredictiveMaintenanceRequest request);
    List<PredictiveMaintenance> getPredictionsByVehicleId(Long vehicleId);
}
