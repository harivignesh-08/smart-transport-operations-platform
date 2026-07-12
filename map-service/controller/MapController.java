package com.transitops.map_service.controller;

import com.transitops.map_service.entity.VehicleLocation;
import com.transitops.map_service.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {

    @Autowired
    private MapService mapService;

    @PostMapping("/location")
    public VehicleLocation saveLocation(@RequestBody VehicleLocation vehicleLocation) {
        return mapService.saveLocation(vehicleLocation);
    }

    @GetMapping("/location/{vehicleId}")
    public VehicleLocation getVehicleLocation(@PathVariable Long vehicleId) {
        return mapService.getVehicleLocation(vehicleId);
    }

    @GetMapping("/locations")
    public List<VehicleLocation> getAllLocations() {
        return mapService.getAllLocations();
    }

    @PutMapping("/location")
    public VehicleLocation updateLocation(@RequestBody VehicleLocation vehicleLocation) {
        return mapService.updateLocation(vehicleLocation);
    }
}