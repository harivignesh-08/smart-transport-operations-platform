package com.transitops.map_service.service;

import com.transitops.map_service.entity.VehicleLocation;
import com.transitops.map_service.repository.VehicleLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {

    private final VehicleLocationRepository repository;

    @Override
    public VehicleLocation saveLocation(VehicleLocation location) {

        location.setUpdatedAt(LocalDateTime.now());

        return repository.save(location);
    }

    @Override
    public List<VehicleLocation> getAllLocations() {
        return repository.findAll();
    }
}