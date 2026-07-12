package com.transitops.digital_twin_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transitops.digital_twin_service.entity.DigitalTwin;
import com.transitops.digital_twin_service.repository.DigitalTwinRepository;

@Service
public class DigitalTwinServiceImpl implements DigitalTwinService {

    @Autowired
    private DigitalTwinRepository repository;

    @Override
    public List<DigitalTwin> getAllDigitalTwins() {
        return repository.findAll();
    }

    @Override
    public DigitalTwin getDigitalTwinById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public DigitalTwin saveDigitalTwin(DigitalTwin digitalTwin) {
        return repository.save(digitalTwin);
    }

    @Override
    public DigitalTwin updateDigitalTwin(Long id, DigitalTwin digitalTwin) {

        DigitalTwin existing = repository.findById(id).orElse(null);

        if (existing != null) {
            existing.setVehicleNumber(digitalTwin.getVehicleNumber());
            existing.setStatus(digitalTwin.getStatus());
            existing.setLocation(digitalTwin.getLocation());

            return repository.save(existing);
        }

        return null;
    }

    @Override
    public void deleteDigitalTwin(Long id) {
        repository.deleteById(id);
    }
}