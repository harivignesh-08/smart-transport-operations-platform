package com.transitops.emergency_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transitops.emergency_service.entity.Emergency;
import com.transitops.emergency_service.repository.EmergencyRepository;

@Service
public class EmergencyServiceImpl implements EmergencyService {

    @Autowired
    private EmergencyRepository repository;

    @Override
    public List<Emergency> getAllEmergencies() {
        return repository.findAll();
    }

    @Override
    public Optional<Emergency> getEmergencyById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Emergency saveEmergency(Emergency emergency) {
        return repository.save(emergency);
    }

    @Override
    public Emergency updateEmergency(Long id, Emergency emergency) {

        Emergency existing = repository.findById(id).orElseThrow();

        existing.setVehicleId(emergency.getVehicleId());
        existing.setEmergencyType(emergency.getEmergencyType());
        existing.setLocation(emergency.getLocation());

        return repository.save(existing);
    }

    @Override
    public void deleteEmergency(Long id) {
        repository.deleteById(id);
    }
}