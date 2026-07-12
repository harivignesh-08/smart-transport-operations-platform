package com.transitops.map_service.repository;

import com.transitops.map_service.entity.VehicleLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleLocationRepository extends JpaRepository<VehicleLocation, Long> {

    Optional<VehicleLocation> findByVehicleId(Long vehicleId);

}