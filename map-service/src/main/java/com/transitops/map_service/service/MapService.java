package com.transitops.map_service.service;

import com.transitops.map_service.entity.VehicleLocation;

import java.util.List;

public interface MapService {

    VehicleLocation saveLocation(VehicleLocation vehicleLocation);

    VehicleLocation getVehicleLocation(Long vehicleId);

    List<VehicleLocation> getAllLocations();

    VehicleLocation updateLocation(VehicleLocation vehicleLocation);

}