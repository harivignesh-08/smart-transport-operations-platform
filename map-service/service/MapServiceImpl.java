package com.transitops.map_service.service;

import com.transitops.map_service.entity.VehicleLocation;
import com.transitops.map_service.repository.VehicleLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MapServiceImpl implements MapService {

    @Autowired
    private VehicleLocationRepository repository;

    @Override
    public VehicleLocation saveLocation(VehicleLocation vehicleLocation) {

        vehicleLocation.setTimestamp(LocalDateTime.now());

        return repository.save(vehicleLocation);
    }

    @Override
    public VehicleLocation getVehicleLocation(Long vehicleId) {

        return repository.findByVehicleId(vehicleId).orElse(null);
    }

    @Override
    public List<VehicleLocation> getAllLocations() {

        return repository.findAll();
    }

    @Override
    public VehicleLocation updateLocation(VehicleLocation vehicleLocation) {

        vehicleLocation.setTimestamp(LocalDateTime.now());

        return repository.save(vehicleLocation);
    }

}