package com.transitops.map_service.repository;

import com.transitops.map_service.entity.VehicleLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleLocationRepository extends JpaRepository<VehicleLocation, Long> {

}